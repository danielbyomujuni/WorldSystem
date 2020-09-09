package de.butzlabben.world.util;

import com.google.common.base.Preconditions;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.util.database.DatabaseProvider;
import de.butzlabben.world.util.database.DatabaseUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/*
Class for implementing #23
 */
public class PlayerPositions {

    @Getter
    public static PlayerPositions instance = new PlayerPositions();

    private final DatabaseUtil util = DatabaseProvider.instance.util;


    private PlayerPositions() {
        checkTables();
    }

    public Location injectWorldsLocation(Player player, WorldConfig config, Location location) {
        if (!PluginConfig.useWorldSpawnLastLocation())
            return location;
        if (!util.isConnectionAvailable())
            return location;

        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(config);
        Preconditions.checkNotNull(location);
        UUID uuid = player.getUniqueId();
        int id = config.getId();
        UUID owner = config.getOwner();
        String tableName = PluginConfig.getWorldsTableName();

        try {
            PreparedStatement ps = util.prepareStatement("SELECT * FROM " + tableName + " WHERE player=? AND id=? AND owner=?");

            ps.setString(1, uuid.toString());
            ps.setInt(2, id);
            ps.setString(3, owner.toString());
            ResultSet rs = util.executeQuery(ps);
            if (!rs.next())
                return location;

            double x = rs.getDouble("x");
            double y = rs.getDouble("y");
            double z = rs.getDouble("z");

            location.setX(x);
            location.setY(y);
            location.setZ(z);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return location;
    }

    public Location injectPlayersLocation(Player player, Location location) {
        if (!PluginConfig.useSpawnLastLocation())
            return location;
        if (!util.isConnectionAvailable())
            return location;
        if (player == null)
            return location;
        Preconditions.checkNotNull(location);
        UUID uuid = player.getUniqueId();

        String tableName = PluginConfig.getPlayersTableName();

        try {
            PreparedStatement ps = util.prepareStatement("SELECT * FROM " + tableName + " WHERE player=?");

            ps.setString(1, uuid.toString());
            ResultSet rs = util.executeQuery(ps);
            if (!rs.next())
                return location;

            double x = rs.getDouble("x");
            double y = rs.getDouble("y");
            double z = rs.getDouble("z");

            location.setX(x);
            location.setY(y);
            location.setZ(z);

            World locationWorld = location.getWorld();
            if (locationWorld == null || !locationWorld.getName().equals(rs.getString("world"))) {
                World world = Bukkit.getWorld(rs.getString("world"));
                if (world != null) {
                    location.setWorld(world);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return location;
    }

    public void savePlayerLocation(Player player) {
        if (!PluginConfig.useSpawnLastLocation())
            return;
        if (!util.isConnectionAvailable())
            return;

        Preconditions.checkNotNull(player);

        String playersTableName = PluginConfig.getPlayersTableName();

        UUID uuid = player.getUniqueId();
        Location location = player.getLocation();
        try {
            PreparedStatement ps = util.prepareStatement("REPLACE INTO " + playersTableName +
                    " (player, world,  x, y, z) VALUES (?, ?, ?, ?, ?)");

            ps.setString(1, uuid.toString());
            ps.setString(2, location.getWorld().getName());

            ps.setDouble(3, location.getX());
            ps.setDouble(4, location.getY());
            ps.setDouble(5, location.getZ());

            util.executeUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveWorldsPlayerLocation(Player player, WorldConfig config) {
        if (!PluginConfig.useWorldSpawnLastLocation())
            return;
        if (!util.isConnectionAvailable())
            return;

        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(config);
        UUID uuid = player.getUniqueId();
        int id = config.getId();
        UUID owner = config.getOwner();
        Location location = player.getLocation();
        String tableName = PluginConfig.getWorldsTableName();
        try {
            PreparedStatement ps = util.prepareStatement("REPLACE INTO " + tableName +
                    " (player, id, owner, x, y, z) VALUES (?, ?, ?, ?, ?, ?)");

            ps.setString(1, uuid.toString());
            ps.setInt(2, id);
            ps.setString(3, owner.toString());
            ps.setDouble(4, location.getX());
            ps.setDouble(5, location.getY());
            ps.setDouble(6, location.getZ());

            util.executeUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePositions(WorldConfig config) {
        Preconditions.checkNotNull(config);
        if (!util.isConnectionAvailable())
            return;

        String tableName = PluginConfig.getWorldsTableName();

        int id = config.getId();
        UUID owner = config.getOwner();
        try {
            PreparedStatement ps = util.prepareStatement("DELETE FROM " + tableName +
                    " WHERE id=? AND owner=?");

            ps.setInt(1, id);
            ps.setString(2, owner.toString());

            util.executeUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkTables() {
        if (!util.isConnectionAvailable())
            return;

        String worldsTableName = PluginConfig.getWorldsTableName();
        try {
            PreparedStatement ps = util.prepareStatement("CREATE TABLE IF NOT EXISTS " + worldsTableName +
                    " ( `player` VARCHAR(36) NOT NULL , `id` INT NOT NULL , `owner` VARCHAR(36) NOT NULL , " +
                    "`x` DOUBLE NOT NULL , `y` DOUBLE NOT NULL , `z` DOUBLE NOT NULL , PRIMARY KEY (`player`, `id`, `owner`))");

            util.executeUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String playersTableName = PluginConfig.getPlayersTableName();
        try {
            PreparedStatement ps = util.prepareStatement("CREATE TABLE IF NOT EXISTS " + playersTableName +
                    "( `player` VARCHAR(36) NOT NULL , `world` TEXT NOT NULL , " +
                    "`x` DOUBLE NOT NULL , `y` DOUBLE NOT NULL , `z` DOUBLE NOT NULL , PRIMARY KEY (`player`))");

            util.executeUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
