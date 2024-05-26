package de.butzlabben.WorldSystem.commands;

import de.butzlabben.WorldSystem.configs.Config;
import de.butzlabben.WorldSystem.configs.LanguageConfig;
import de.butzlabben.WorldSystem.data.WorldSystemData;
import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.wrapper.SystemWorld;
import de.butzlabben.WorldSystem.worldgen.WorldTemplate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CreateWorldCommand {

    public CreateWorldCommand(Player player) {
        WorldSystemData ws_data = WorldSystemData.connect();
        int world_count = ws_data.getWorldCountForPlayer(player.getUniqueId().toString());

        if (world_count < Config.getPlayerWorldCountLimit()) {
            player.sendMessage(LanguageConfig.getWorldAlreadyExists());
        }

        if (Config.AllowTemplateChoice()) {
            //TODO
        } else {
            create_default_world(player);
        }
    }

    private void create_default_world(Player player) {
        //get default template from world generator
        WorldTemplate template = WorldTemplate.default_template();
        //then create
        create_world(player, template);
    }

    private void create_world(Player player, WorldTemplate template ) {
        Bukkit.getScheduler().runTask(WorldSystem.getInstance(), () -> {
            //create the world
            if (template.create(player)) { //add settings;
                player.sendMessage(MessageConfig.getSettingUpWorld());
            }
        });
    }

}
