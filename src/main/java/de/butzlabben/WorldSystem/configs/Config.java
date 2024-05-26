package de.butzlabben.WorldSystem.configs;

public class Config {
    private static ConfigData data;

    private static ConfigData get_config() {
        if (data == null) {
            data = new ConfigData();
        }
        return data;
    }

    public static String getLanguage() {return get_config().locale;}


    public static boolean AllowTemplateChoice() {return get_config().allow_template_choice;}
    public static int getPlayerWorldCountLimit() {return get_config().player_world_limit;}





}

class ConfigData {
    //general
    public String locale;

    //player world stuff
    public boolean allow_template_choice;
    public int player_world_limit;


    public ConfigData() {
        //load the yaml file


        //set data
        this.locale = "en";


        this.allow_template_choice = false;
        this.player_world_limit = 1;
    }
}
