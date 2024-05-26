package de.butzlabben.WorldSystem.worldgen;

import de.butzlabben.WorldSystem.data.WorldSystemData;
import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.*;
import de.butzlabben.world.event.WorldCreateEvent;
import de.butzlabben.world.wrapper.SystemWorld;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class WorldTemplate {



    public static WorldTemplate default_template() {
        return new WorldTemplate();
    }

    public boolean create(Player player) {

        WorldSystemData data = WorldSystemData.connect();

        String uuid = player.getUniqueId().toString();

        int id = data.getHighestID() + 1;
        String worldname = "ID" + id + "-" + uuid;

        //create an event to different hooks

        //create a new database entry for the world


        //setup the world folder


        //create a new database entry for the worlds data


        //load the world for bukkit

        //lock the world world as createing


        //establish a async task for world generation.



/*
        WorldCreateEvent event = new WorldCreateEvent(uniqueID, creator);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return false;

        dc.createNewEntry();

        String worlddir = PluginConfig.getWorlddir();
        File exampleworld = new File(template.getPath());
        if (new File(template.getPath() + "/uid.dat").exists()) {
            new File(template.getPath() + "/uid.dat").delete();
        }

        File newworld = new File(worlddir + "/" + worldname);

        if (exampleworld.isDirectory())
            try {
                FileUtils.copyDirectory(exampleworld, newworld);
            } catch (IOException e) {
                WorldSystem.logger().log(Level.SEVERE,"Couldn't create world for " + uuid);
                e.printStackTrace();
            }
        else
            newworld.mkdirs();

        WorldConfig.create(uniqueID, template);

        // Move World into Server dir
        File world = new File(worlddir + "/" + worldname);
        if (!world.exists()) {
            world = new File(Bukkit.getWorldContainer(), worldname);
        } else {
            if (new File(Bukkit.getWorldContainer(), worldname).exists()
                    && new File(PluginConfig.getWorlddir() + "/" + worldname).exists()) {
                try {
                    FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer(), worldname));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileUtils.moveDirectoryToDirectory(world, Bukkit.getWorldContainer(), false);
            } catch (IOException e) {
                if (p != null && p.isOnline())
                    p.sendMessage(PluginConfig.getPrefix() + "§cError: " + e.getMessage());
                WorldSystem.logger().log(Level.SEVERE,"Couldn't load world of " + uuid);
                e.printStackTrace();
                return false;
            }
        }

        SystemWorld sw = SystemWorld.getSystemWorld(worldname);
        sw.setCreating(true);

        // Run in scheduler so method returns without delay
        new BukkitRunnable() {
            @Override
            public void run() {
                WorldSystem.getInstance().getAdapter().create(event.getWorldCreator(), sw, () -> {
                    // Fix for #16
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (p != null && p.isOnline()) {
                                p.sendMessage(MessageConfig.getWorldCreated());
                                SettingsConfig.getCommandsonGet().stream()
                                        .map(s -> s.replace("%player", p.getName()).replace("%world", sw.getName())
                                                .replace("%uuid", p.getUniqueId().toString()))
                                        .forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s));
                            }
                        }
                    }.runTask(WorldSystem.getInstance());
                });
            }
        }.runTaskLater(WorldSystem.getInstance(), 1);

        return true;
        */
        return false;
    }
}
