package de.butzlabben.world.event;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.wrapper.SystemWorld;
import de.butzlabben.world.wrapper.WorldTemplate;
import de.butzlabben.world.wrapper.WorldTemplateProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class ServerPlayerJoinEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        System.out.println("creating world for " + event.getPlayer().getDisplayName());
        if (PluginConfig.canCreateWorldOnJoin()) {
            if (!SystemWorld.hasWorld(event.getPlayer())) {

                WorldTemplate template = WorldTemplateProvider.getInstance().getTemplate(PluginConfig.getDefaultWorldTemplate());
                if (template != null) {
                    //event.getPlayer().sendMessage(PluginConfig.getPrefix() + "Creating you world");
                    this.create(event.getPlayer(), template);
                } else {
                    event.getPlayer().sendMessage(PluginConfig.getPrefix() + "§cError in config at \"worldtemplates.default\"");
                    event.getPlayer().sendMessage(PluginConfig.getPrefix() + "§cPlease contact an administrator");
                }
            }
        }
    }
        private void create (Player p, WorldTemplate template){
            Bukkit.getScheduler().runTask(WorldSystem.getInstance(), () -> {
                if (SystemWorld.create(p, template))
                    p.sendMessage(MessageConfig.getSettingUpWorld());
            });
        }

}
