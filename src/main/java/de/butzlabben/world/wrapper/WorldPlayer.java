package de.butzlabben.world.wrapper;

import com.google.common.base.Preconditions;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.config.WorldConfig;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * This class represents a player, on a systemworld or not but be carefull when
 * accesing some methods when the player is not on a systemworld like
 * toggleBuild()
 *
 * @author Butzlabben
 * @since 15.03.2018
 */
public class WorldPlayer {

    private final OfflinePlayer p;
    private final String worldname;

    public WorldPlayer(OfflinePlayer p, String worldname) {
        this.p = p;
        this.worldname = worldname;
    }

    public WorldPlayer(Player p) {
        this(p, p.getWorld().getName());
    }

    /**
     * @return the worldname, where the worldplayer object was created for
     */
    public String getWorldname() {
        return worldname;
    }

    /**
     * toggles building for this player
     *
     * @return whether can build or not
     */
    public boolean toggleBuild() {
        Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");

        WorldConfig wc = WorldConfig.getWorldConfig(worldname);
        if (!wc.isMember(p.getUniqueId()))
            return false;

        boolean build = wc.canBuild(p.getUniqueId());
        build = !build;
        wc.setBuild(p.getUniqueId(), build);
        try {
            wc.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return build;
    }

    /**
     * @return whether can build or not
     */
    public boolean canBuild() {
        Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");
        WorldConfig wc = WorldConfig.getWorldConfig(worldname);
        return wc.canBuild(p.getUniqueId());
    }

    public boolean toggleWorldEdit() {
        Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");

        WorldConfig wc = WorldConfig.getWorldConfig(this.worldname);
        if (!wc.isMember(this.p.getUniqueId())) {
            return false;
        }
        boolean build = wc.canWorldEdit(this.p.getUniqueId());
        build = !build;
        wc.setWorldEdit(this.p.getUniqueId(), build);
        try {
            wc.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return build;
    }

    public boolean canWorldedit() {
        Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");
        WorldConfig wc = WorldConfig.getWorldConfig(this.worldname);
        return wc.canWorldEdit(this.p.getUniqueId());
    }

    /**
     * toggles teleporting for this player
     *
     * @return whether can teleport or not
     */
    public boolean toggleTeleport() {
        Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");

        WorldConfig wc = WorldConfig.getWorldConfig(worldname);
        if (!wc.isMember(p.getUniqueId()))
            return false;

        boolean teleport = wc.canTeleport(p.getUniqueId());
        teleport = !teleport;
        wc.setTeleport(p.getUniqueId(), teleport);
        try {
            wc.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teleport;
    }

    /**
     * @return whether can teleport or not
     */
    public boolean canTeleport() {
        Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");
        WorldConfig wc = WorldConfig.getWorldConfig(worldname);
        return wc.canTeleport(p.getUniqueId());
    }

    /**
     * toggles gamemode changing for this player
     *
     * @return whether can change his gamemode or not
     */
    public boolean toggleGamemode() {
        Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");

        WorldConfig wc = WorldConfig.getWorldConfig(worldname);
        if (!wc.isMember(p.getUniqueId()))
            return false;

        boolean changeGamemode = wc.canGamemode(p.getUniqueId());
        changeGamemode = !changeGamemode;
        wc.setGamemode(p.getUniqueId(), changeGamemode);
        try {
            wc.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return changeGamemode;
    }

    /**
     * @return whether can change his gamemode or not
     */
    public boolean canChangeGamemode() {
        Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");
        WorldConfig wc = WorldConfig.getWorldConfig(worldname);
        return wc.canGamemode(p.getUniqueId());
    }

    /**
     * @return if he is a member
     */
    public boolean isMember() {
        Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");

        WorldConfig wc = WorldConfig.getWorldConfig(worldname);
        return wc.isMember(p.getUniqueId());
    }

    /**
     * @return if he is on a systemworld or not
     */
    public boolean isOnSystemWorld() {
        File worldconfig = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
        if (!worldconfig.exists()) {
            worldconfig = new File(PluginConfig.getWorlddir() + worldname + "/worldconfig.yml");
        }
        if (worldconfig.exists()) {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(worldconfig);
            return cfg.getString("Informations.Owner.PlayerUUID") != null;
        }
        return false;
    }

    /**
     * @return the given player
     */
    public OfflinePlayer getPlayer() {
        return p;
    }

    /**
     * @return if he ist the owner
     */
    public boolean isOwnerofWorld() {
        if (!isOnSystemWorld())
            return false;
        WorldConfig wc = WorldConfig.getWorldConfig(worldname);
        return wc.getOwner().equals(p.getUniqueId());
    }

    /**
     * @param worldname of the world to be tested
     * @return worldname if he is the owner of the specified world
     */
    public boolean isMemberofWorld(String worldname) {
        WorldConfig wc = WorldConfig.getWorldConfig(worldname);
        return wc.isMember(p.getUniqueId());
    }

}
