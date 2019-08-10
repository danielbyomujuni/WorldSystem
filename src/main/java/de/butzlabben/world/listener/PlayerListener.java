package de.butzlabben.world.listener;

import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.util.PlayerPositions;
import de.butzlabben.world.wrapper.SystemWorld;
import de.butzlabben.world.wrapper.WorldPlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    //#17
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        if (PluginConfig.spawnTeleportation()) {
            Player p = e.getPlayer();
            DependenceConfig dc = new DependenceConfig(p);
            if (dc.hasWorld()) {
                SystemWorld sw = SystemWorld.getSystemWorld(dc.getWorldname());
                if (sw != null && !sw.isLoaded()) {
                    e.getPlayer().teleport(PluginConfig.getSpawn(e.getPlayer()));
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        World w = p.getWorld();
        WorldPlayer player = new WorldPlayer(p);
        // Save last location for #23
        if (player.isOnSystemWorld()) {
            WorldConfig config = WorldConfig.getWorldConfig(player.getWorldname());
            PlayerPositions.getInstance().saveWorldsPlayerLocation(p, config);
        }
        SystemWorld.tryUnloadLater(w);
    }
}