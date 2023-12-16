package de.butzlabben.world.util;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.wrapper.SystemWorld;
import de.butzlabben.world.wrapper.WorldPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PapiExtension extends PlaceholderExpansion {

    private final WorldSystem worldSystem = WorldSystem.getInstance();

    @Override
    public @NotNull String getAuthor() {
        return "CrazyCloudCraft";
    }

        @Override
    public @NotNull String getIdentifier() {
        return "worldsystem";
    }

    @Override
    public @NotNull String getVersion() {
        return worldSystem.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("name")) {
            return player == null ? null : player.getName(); // "name" requires the player to be valid
        }
        
        if(params.equalsIgnoreCase("placeholder1")) {
            return "Ich bin ne Placeholder du lappn";
        }
        
        if(params.equalsIgnoreCase("placeholder2")) {
            return "Placeholder Text 2";
        }
        
        return null; // Placeholder is unknown by the Expansion
    }/*

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        DependenceConfig config = new DependenceConfig(player);

        String[] args = params.split("_");
        if (args.length == 3 && args[0].equalsIgnoreCase("world")
                && args[1].equalsIgnoreCase("member")){

            Player playerOnline = player.getPlayer();
            if(playerOnline == null) return "none";
            WorldConfig wc = WorldConfig.getWorldConfig(playerOnline.getWorld().getName());
            List<String> members = new ArrayList<>(wc.getMembersWithNames().values());
            int member;
            try {
                member = Integer.parseInt(args[2]);
            } catch (NumberFormatException e){
                throw new IllegalArgumentException("No placeholder named\"" + getIdentifier() + "_" + params + "\" is known");
            }

            return members.get(member);
        }
        switch (params) {
            case "has_world":
                return new DependenceConfig(p).hasWorld() + "";
// @@ -48,16 +69,16 @@ public String onRequest(OfflinePlayer p, String params) {
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
            case "world_loaded":
                if (!config.hasWorld())
                    return "none";
                return Objects.requireNonNull(SystemWorld.getSystemWorld(config.getWorldname())).isLoaded() + "";
            case "pretty_world_name":
                if (!p.isOnline()) {
                    if (!config.hasWorld()) {
                        Player p1 = p.getPlayer();
                        if (p1 != null && p1.isOnline())
                            return Objects.requireNonNull(p1.getLocation().getWorld()).getName();
                        return "none";
                    }
                    return config.getOwner().getName();
                } else {
                    World world = ((Player) p).getWorld();
                    if (WorldConfig.exists(world.getName()))
                        return WorldConfig.getWorldConfig(world.getName()).getOwnerName();
                    return world.getName();
                }
            default:
                throw new IllegalArgumentException("No placeholder named\"" + getIdentifier() + "_" + params + "\" is known");
        }*/
    }
}
