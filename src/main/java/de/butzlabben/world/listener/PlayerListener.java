package de.butzlabben.world.listener;

import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.util.PlayerPositions;
import de.butzlabben.world.util.PlayerWrapper;
import de.butzlabben.world.wrapper.SystemWorld;
import de.butzlabben.world.wrapper.WorldPlayer;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerListener implements Listener {

    private final HashMap<UUID, World> deathLocations = new HashMap<>();

    //#17
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        PlayerWrapper.updateDatabase(e.getPlayer());
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

    @EventHandler
    public void onDie(PlayerDeathEvent e) {
        Player p = e.getEntity();
        WorldPlayer wp = new WorldPlayer(p, p.getWorld().getName());
        if (wp.isOnSystemWorld()) {
            deathLocations.put(p.getUniqueId(), p.getLocation().getWorld());
        } else {
            p.setGameMode(PluginConfig.getSpawnGamemode());
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (deathLocations.containsKey(p.getUniqueId())) {
            World world = deathLocations.remove(p.getUniqueId());

            WorldConfig config = WorldConfig.getWorldConfig(world.getName());

            if (config.getHome() != null) {
                e.setRespawnLocation(config.getHome());
            } else {
                if (PluginConfig.useWorldSpawn()) {
                    e.setRespawnLocation(PluginConfig.getWorldSpawn(world));
                } else {
                    e.setRespawnLocation(world.getSpawnLocation());
                }
            }
        }
    }

}