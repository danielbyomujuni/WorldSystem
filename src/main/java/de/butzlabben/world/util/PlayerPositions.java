package de.butzlabben.world.util;

import com.google.common.base.Preconditions;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.util.database.DatabaseRepository;
import de.butzlabben.world.util.database.DatabaseUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/*
Class for implementing #23
 */
@Getter
public class PlayerPositions {

    @Getter
    private static PlayerPositions instance = new PlayerPositions();

    private DatabaseUtil util = DatabaseRepository.getInstance().getUtil();

    public Location injectLocation(Player player, WorldConfig config, Location location) {
        if (!PluginConfig.useLastLocation())
            return location;

        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(config);
        Preconditions.checkNotNull(location);
        UUID uuid = player.getUniqueId();
        int id = config.getId();
        UUID owner = config.getOwner();
        String tableName = PluginConfig.getTableName();

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

    public void savePlayerLocation(Player player, WorldConfig config) {
        if (!PluginConfig.useLastLocation())
            return;

        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(config);
        UUID uuid = player.getUniqueId();
        int id = config.getId();
        UUID owner = config.getOwner();
        Location location = player.getLocation();
        String tableName = PluginConfig.getTableName();
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
        String tableName = PluginConfig.getTableName();

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

    private void checkTables() {
        String tableName = PluginConfig.getTableName();
        try {
            PreparedStatement ps = util.prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName +
                    " ( `player` VARCHAR(36) NOT NULL , `id` INT NOT NULL , `owner` VARCHAR(36) NOT NULL , " +
                    "`x` DOUBLE NOT NULL , `y` DOUBLE NOT NULL , `z` DOUBLE NOT NULL , PRIMARY KEY (`player`, `id`, `owner`))");

            util.executeUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PlayerPositions() {
        checkTables();
    }
}
