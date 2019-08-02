package de.butzlabben.world.util;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.wrapper.SystemWorld;
import de.butzlabben.world.wrapper.WorldPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PapiExtension extends PlaceholderExpansion {

    private final WorldSystem worldSystem = WorldSystem.getInstance();

    @Override
    public String getIdentifier() {
        return "worldsystem";
    }

    @Override
    public String getAuthor() {
        return "Butzlabben";
    }

    @Override
    public String getVersion() {
        return worldSystem.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer p, String params) {
        DependenceConfig config = new DependenceConfig(p);
        switch (params) {
            case "has_world":
                return new DependenceConfig(p).hasWorld() + "";
            case "is_creator":
                WorldPlayer player = new WorldPlayer(Objects.requireNonNull(Bukkit.getPlayer(p.getUniqueId())));
                if (!player.isOnSystemWorld())
                    return "false";
                return player.isOwnerofWorld() + "";
            case "world_name_of_player":
                if (!config.hasWorld())
                    return "none";
                else
                    return config.getWorldname();
            case "world_of_player_loaded":
                if (!config.hasWorld())
                    return "none";
                return SystemWorld.getSystemWorld(config.getWorldname()).isLoaded() + "";
            case "pretty_world_name":
                if (!config.hasWorld()) {
                    Player p1 = p.getPlayer();
                    if (p1 != null && p1.isOnline())
                        return p1.getLocation().getWorld().getName();
                    return "none";
                }
                return config.getOwner().getName();
            default:
                throw new IllegalArgumentException("No placeholder named\"" + getIdentifier() + "_" + params + "\" is known");
        }
    }
}
