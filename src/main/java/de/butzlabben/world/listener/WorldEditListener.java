package de.butzlabben.world.listener;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extension.platform.CommandManager;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.config.WorldPerm;
import de.butzlabben.world.wrapper.WorldPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class WorldEditListener implements Listener {

    @EventHandler
    public void playerCommandHandler(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0];
        if (!isWorldEditCommand(command)) {
            return;
        }
        Player p = event.getPlayer();
        String worldname = p.getWorld().getName();
        WorldPlayer wp = new WorldPlayer(p, worldname);
        if (wp.isOnSystemWorld() &&
                !wp.isOwnerofWorld() && !p.hasPermission(WorldPerm.WORLDEDIT.getOpPerm())) {
            WorldConfig wc = WorldConfig.getWorldConfig(p.getWorld().getName());
            if (!wc.hasPermission(p.getUniqueId(), WorldPerm.WORLDEDIT)) {
                p.sendMessage(MessageConfig.getNoPermission());
                event.setCancelled(true);
            }
        }
    }

    private boolean isWorldEditCommand(String command) {
        WorldEditPlugin plugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        // WorldEdit plugin not foung
        if(plugin == null)
            return false;
        if (command.startsWith("/")) {
            command = command.replaceFirst("/", "");
        }

        return plugin.getCommand(command) != null;
    }
}
