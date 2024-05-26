package de.butzlabben.WorldSystem;


//once all old code is deprecated this will become the new plugin core

import co.aikar.commands.BukkitCommandManager;
import de.butzlabben.WorldSystem.commands.WSCommands;
import de.butzlabben.WorldSystem.configs.LanguageConfig;

public class WorldSystem { //TODO extends JavaPlugin

    private static final String LOCALE = "en";



    //TODO: @Override
    public void onEnable() {
        //create configs
        create_configs();
        //register commands
        BukkitCommandManager manager = new BukkitCommandManager(getInstance()); //TODO this);
        manager.registerCommand(new WSCommands());
    }


    public static de.butzlabben.world.WorldSystem getInstance() {
        return de.butzlabben.world.WorldSystem.getInstance();
    }

    private void create_configs() {
        //Establish the language for the plugin
        LanguageConfig.verify_all_configs();
        LanguageConfig.set_config(LOCALE);
    }





}
