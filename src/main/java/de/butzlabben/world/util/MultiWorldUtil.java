package de.butzlabben.world.util;

import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.PluginConfig;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MultiWorldUtil {
    //Done
    Dictionary playerWorldList = new Hashtable();

    public static File getWorldList(String uuid) {
        File worldList = new File(PluginConfig.getWorlddir()+ "/" + uuid  + "/worldlist.yml");
        if (!worldList.exists()) {
            worldList = new File(PluginConfig.getWorlddir() + "/" + uuid + "/worldlist.yml");
        }
        if (!worldList.exists()) {
            worldList = new File(uuid  + "/worldlist.yml");
        }
        return worldList;
    }


    public static void create(UUID uuid) {
        DependenceConfig dc = new DependenceConfig(uuid);
        String worldname = dc.getWorldname();
        File file = new File(PluginConfig.getWorlddir() + uuid + "/worldlist.yml");
        try {
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
            System.err.println("Error while creating worldconfig for " + uuid.toString());
        }

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        Dictionary worlds = new Hashtable();
        File dirc = new File(PluginConfig.getWorlddir() + uuid + "/");
        String[] names = dirc.list();

        for(String name : names)
        {
            if (new File(dirc + name).isDirectory())
            {
                worlds.put(name, new File(dirc + name));
            }
        }
        cfg.set("WorldList", worlds);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while saving worldconfig for " + uuid.toString());
        }
    }

    public MultiWorldUtil save(UUID uuid) throws IOException {
        File file = new File(PluginConfig.getWorlddir() + uuid + "/worldlist.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        Dictionary worlds = new Hashtable();
        File dirc = new File(PluginConfig.getWorlddir() + uuid + "/");
        String[] names = dirc.list();

        for(String name : names)
        {
            if (new File(dirc + name).isDirectory())
            {
                worlds.put(name, new File(dirc + name));
            }
        }
        cfg.set("WorldList", worlds);

        cfg.save(file);
        return this;
    }


    public MultiWorldUtil load(UUID uuid) {
        File file = new File(PluginConfig.getWorlddir() + uuid + "/worldlist.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        playerWorldList = (Dictionary) cfg.get("WorldList", "Unknown World");
            try {
                save(uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return this;
    }

    public Dictionary getworlds() {
        return playerWorldList;
    }

}
