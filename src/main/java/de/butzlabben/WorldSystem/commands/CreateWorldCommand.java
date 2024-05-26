package de.butzlabben.WorldSystem.commands;

import de.butzlabben.WorldSystem.configs.LanguageConfig;
import de.butzlabben.WorldSystem.data.WorldSystemData;
import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.MessageConfig;
import org.bukkit.entity.Player;

public class CreateWorldCommand {

    private static final int WORLD_COUNT_LIMIT = 1; //REPLACE WITH CONFIG
    private static final boolean ALLOW_TEMPLATE_CHOICE = false; //REPLACE WITH CONFIG


    public CreateWorldCommand(Player player) {
        WorldSystemData ws_data = WorldSystemData.connect();
        int world_count = ws_data.getWorldCountForPlayer(player.getUniqueId().toString());

        if (world_count < WORLD_COUNT_LIMIT) {
            player.sendMessage(LanguageConfig.getWorldAlreadyExists());
        }

        if (ALLOW_TEMPLATE_CHOICE) {
            //TODO
        } else {
            create_default_world();
        }
    }

    private void create_default_world() {
        //get default template from world generator
        //then create
        create_world();
    }

    private void create_world() {

    }

}
