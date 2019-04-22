package de.butzlabben.world.listener;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.config.WorldPerm;
import de.butzlabben.world.wrapper.WorldPlayer;
import org.bukkit.Bukkit;
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
        if (command.startsWith("/")) {
            command = command.replaceFirst("/", "");
        }
        command = command.toLowerCase();
        return ((WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit")).getWorldEdit().getPlatformManager()
                .getCommandManager().getDispatcher().get(command) != null;
    }
}
