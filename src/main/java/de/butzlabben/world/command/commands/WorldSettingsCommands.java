package de.butzlabben.world.command.commands;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.event.WorldResetEvent;
import de.butzlabben.world.event.WorldToggleFireEvent;
import de.butzlabben.world.event.WorldToggleTntEvent;
import de.butzlabben.world.gui.WorldChooseGUI;
import de.butzlabben.world.util.PlayerPositions;
import de.butzlabben.world.wrapper.SystemWorld;
import de.butzlabben.world.wrapper.WorldPlayer;
import de.butzlabben.world.wrapper.WorldTemplate;
import de.butzlabben.world.wrapper.WorldTemplateProvider;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WorldSettingsCommands {
    private static final ArrayList<Player> toConfirm = new ArrayList<>();


    public boolean resetCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

        DependenceConfig dc = new DependenceConfig(p);
        String worldname = dc.getWorldname();
        SystemWorld sw = SystemWorld.getSystemWorld(worldname);
        if (!dc.hasWorld()) {
            p.sendMessage(MessageConfig.getNoWorldOwn());
            return false;
        }
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("confirm")) {
                if (sw.isLoaded())
                    sw.directUnload(Bukkit.getWorld(worldname));

                if (!toConfirm.contains(p)) {
                    p.sendMessage(MessageConfig.getNoRequestSend());
                    return false;
                }
                WorldResetEvent event = new WorldResetEvent(p, sw);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled())
                    return false;

                if (sw.isLoaded()) {
                    p.sendMessage(MessageConfig.getWorldStillLoaded());
                    return false;
                }

                WorldConfig config = WorldConfig.getWorldConfig(worldname);
                // Delete positions to prevent suffocating and such stuff
                PlayerPositions.instance.deletePositions(config);

                File f = new File(PluginConfig.getWorlddir() + "/" + worldname);

                if (!PluginConfig.isMultiChoose()) {
                    WorldTemplate template = WorldTemplateProvider.getInstance()
                            .getTemplate(PluginConfig.getDefaultWorldTemplate());
                    if (template != null)
                        createWorld(p, worldname, f, template, sw);
                    else {
                        p.sendMessage(PluginConfig.getPrefix() + "§cError in config at \"worldtemplates.default\"");
                        p.sendMessage(PluginConfig.getPrefix() + "§cPlease contact an administrator");
                    }
                } else {
                    WorldChooseGUI.letChoose(p, (template) -> {
                        if (template != null)
                            createWorld(p, worldname, f, template, sw);
                        else {
                            p.sendMessage(PluginConfig.getPrefix() + "§cError in config at \"worldtemplates.default\"");
                            p.sendMessage(PluginConfig.getPrefix() + "§cPlease contact an administrator");
                        }
                    });
                }

            } else {
                p.sendMessage(MessageConfig.getInvalidInput().replaceAll("input", args[1]));
            }
        } else {
            if (sw.isLoaded())
                sw.directUnload(Bukkit.getWorld(worldname));

            if (toConfirm.contains(p)) {
                p.sendMessage(MessageConfig.getRequestAlreadySent());
                return false;
            }

            int time = PluginConfig.getRequestExpire();
            p.sendMessage(MessageConfig.getConfirmRequest().replaceAll("%command", "/ws reset confirm"));
            p.sendMessage(MessageConfig.getTimeUntilExpires().replaceAll("%time", String.valueOf(time)));
            toConfirm.add(p);
            Bukkit.getScheduler().runTaskLater(WorldSystem.getInstance(), () -> {
                if (toConfirm.contains(p)) {
                    p.sendMessage(MessageConfig.getRequestExpired());
                    toConfirm.remove(p);
                }
            }, time * 20L);
        }
        return true;
    } else {
        sender.sendMessage("No Console"); //TODO Get Config
        return false;
    }
    }

    public boolean setHomeCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            WorldPlayer wp = new WorldPlayer(p);
            if (!wp.isOnSystemWorld()) {
                p.sendMessage(MessageConfig.getNotOnWorld());
                return false;
            }
            if (!wp.isOwnerofWorld()) {
                p.sendMessage(MessageConfig.getNoPermission());
                return false;
            }

            if (!p.hasPermission("ws.sethome")) {
                p.sendMessage(MessageConfig.getNoPermission());
                return false;
            }

            WorldConfig config = WorldConfig.getWorldConfig(p.getWorld().getName());
            Location playerLocation = p.getLocation();
            config.home = playerLocation;
            System.out.println("installed");
            try {
                if (config.home == playerLocation) {
                    System.out.println("registered");
                } else {
                    System.out.println("registered incorrectly");
                }
                config.save();
                p.sendMessage(MessageConfig.getHomeSet());
            } catch (IOException e) {
                p.sendMessage(MessageConfig.getUnknownError() + ": " + e.getMessage());
                e.printStackTrace();
            }
            return true;
        } else {
            sender.sendMessage("No Console"); //TODO Get Config
            return false;
        }
    }

    public boolean tntCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
            DependenceConfig dc = new DependenceConfig(p);
            WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname());
            boolean tnt = wc.isTnt();
            WorldToggleTntEvent event = new WorldToggleTntEvent(p, SystemWorld.getSystemWorld(dc.getWorldname()), tnt);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled())
                return false;

            wc.setTnt(p.getUniqueId(), !tnt);
            try {
                wc.save();
            } catch (IOException e) {
                p.sendMessage(PluginConfig.getPrefix() + "§cSomething went wrong");
                e.printStackTrace();
            }
            tnt = wc.isTnt();
            if (tnt) {
                p.sendMessage(MessageConfig.getToggleTntEnabled());
            } else {
                p.sendMessage(MessageConfig.getToggleTntDisabled());
            }
                return true;
            } else {
                sender.sendMessage("No Console"); //TODO Get Config
                return false;
            }
        }

    public boolean fireCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
        DependenceConfig dc = new DependenceConfig(p);
        WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname());
        boolean fire = wc.isFire();
        WorldToggleFireEvent event = new WorldToggleFireEvent(p, SystemWorld.getSystemWorld(dc.getWorldname()), fire);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return false;

        wc.setFire(p.getUniqueId(), !fire);
        try {
            wc.save();
        } catch (IOException e) {
            p.sendMessage(PluginConfig.getPrefix() + "§cSomething went wrong");
            e.printStackTrace();
        }
        fire = wc.isFire();
        if (fire) {
            p.sendMessage(MessageConfig.getToggleFireEnabled());
        } else {
            p.sendMessage(MessageConfig.getToggleFireDisabled());
        }
            return true;
        } else {
            sender.sendMessage("No Console"); //TODO Get Config
            return false;
        }
    }











    private void createWorld(Player p, String worldname, File f, WorldTemplate template, SystemWorld sw) {

        if (f != null) {
            File[] files = f.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().equals("worldconfig.yml"))
                        continue;
                    FileUtils.deleteQuietly(file);
                }
            }
        }

        try {
            File templateDirectory = new File(template.getPath());
            if (templateDirectory.isDirectory())
                FileUtils.copyDirectory(templateDirectory, f);
            toConfirm.remove(p);

            FileUtils.moveDirectoryToDirectory(f, Bukkit.getWorldContainer(), false);

            WorldConfig config = WorldConfig.getWorldConfig(worldname);
            config.home = null;
            config.setTemplateKey(template.getName());
            config.save();

            p.sendMessage(MessageConfig.getWorldReseted());

            // For fast worldcreating after reset
            WorldCreator creator = template.generatorSettings.asWorldCreator(worldname);

            sw.setCreating(true);
            // For #16
            WorldSystem.getInstance().getAdapter().create(creator, sw, () -> {
                if (p != null && p.isOnline())
                    p.sendMessage(MessageConfig.getWorldCreated());
            });

        } catch (IOException e) {
            e.printStackTrace();
            p.sendMessage(MessageConfig.getUnknownError());
            System.err.println("Couldn't reset world of " + p.getName());
        }
    }
}
