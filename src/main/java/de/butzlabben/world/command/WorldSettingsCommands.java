package de.butzlabben.world.command;

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
import net.myplayplanet.commandframework.CommandArgs;
import net.myplayplanet.commandframework.api.Command;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WorldSettingsCommands {

    private final ArrayList<Player> toConfirm = new ArrayList<>();

    @Command(name="ws.reset", inGameOnly = true, usage = "/ws reset [confirm]")
    public void resetCommand(CommandArgs args) {
        Player p = args.getSender(Player.class);

        DependenceConfig dc = new DependenceConfig(p);
        String worldname = dc.getWorldname();
        SystemWorld sw = SystemWorld.getSystemWorld(worldname);
        if (!dc.hasWorld()) {
            p.sendMessage(MessageConfig.getNoWorldOwn());
            return;
        }
        if (args.length() > 0) {
            if (args.getArgument(0).equalsIgnoreCase("confirm")) {
                if (sw.isLoaded())
                    sw.directUnload(Bukkit.getWorld(worldname));

                if (!toConfirm.contains(p)) {
                    p.sendMessage(MessageConfig.getNoRequestSend());
                    return;
                }
                WorldResetEvent event = new WorldResetEvent(p, sw);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled())
                    return;

                if (sw.isLoaded()) {
                    p.sendMessage(MessageConfig.getWorldStillLoaded());
                    return;
                }

                WorldConfig config = WorldConfig.getWorldConfig(worldname);
                // Delete positions to prevent suffocating and such stuff
                PlayerPositions.getInstance().deletePositions(config);

                File f = new File(PluginConfig.getWorlddir() + "/" + worldname);

                if (!PluginConfig.isMultiChoose()) {
                    WorldTemplate template = WorldTemplateProvider.getInstace()
                            .getTemplate(PluginConfig.getDefaultWorldTemplate());
                    if (template != null)
                        createWorld(p, worldname, f, new File(template.getPath()), sw);
                    else {
                        p.sendMessage(PluginConfig.getPrefix() + "§cError in config at \"worldtemplates.default\"");
                        p.sendMessage(PluginConfig.getPrefix() + "§cPlease contact an administrator");
                    }
                } else {
                    WorldChooseGUI.letChoose(p, (template) -> {
                        if (template != null)
                            createWorld(p, worldname, f, new File(template.getPath()), sw);
                        else {
                            p.sendMessage(PluginConfig.getPrefix() + "§cError in config at \"worldtemplates.default\"");
                            p.sendMessage(PluginConfig.getPrefix() + "§cPlease contact an administrator");
                        }
                    });
                }

            } else {
                p.sendMessage(MessageConfig.getInvalidInput().replaceAll("input", args.getArgument(0)));
            }
        } else {
            if (sw.isLoaded())
                sw.directUnload(Bukkit.getWorld(worldname));

            if (toConfirm.contains(p)) {
                p.sendMessage(MessageConfig.getRequestAlreadySent());
                return;
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
    }

    @Command(name = "ws.sethome", inGameOnly = true, usage = "/ws sethome")
    public void setHomeCommand(CommandArgs args) {
        Player p = args.getSender(Player.class);

        WorldPlayer wp = new WorldPlayer(p);
        if (!wp.isOnSystemWorld()) {
            p.sendMessage(MessageConfig.getNotOnWorld());
            return;
        }
        if (!wp.isOwnerofWorld()) {
            p.sendMessage(MessageConfig.getNoPermission());
            return;
        }

        if(!p.hasPermission("ws.sethome")) {
            p.sendMessage(MessageConfig.getNoPermission());
            return;
        }

        WorldConfig config = WorldConfig.getWorldConfig(p.getWorld().getName());
        config.setHome(p.getLocation());
        try {
            config.save();
            p.sendMessage(MessageConfig.getHomeSet());
        } catch (IOException e) {
            p.sendMessage(MessageConfig.getUnknownError() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Command(name = "ws.tnt", inGameOnly = true, usage = "/ws tnt")
    public void tntCommand(CommandArgs args) {
        Player p = args.getSender(Player.class);

        DependenceConfig dc = new DependenceConfig(p);
        WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname());
        boolean tnt = wc.isTnt();
        WorldToggleTntEvent event = new WorldToggleTntEvent(p, SystemWorld.getSystemWorld(dc.getWorldname()), tnt);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

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
    }

    @Command(name = "ws.fire", inGameOnly = true, usage = "/ws fire")
    public void fireCommand(CommandArgs args) {
        Player p = args.getSender(Player.class);

        DependenceConfig dc = new DependenceConfig(p);
        WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname());
        boolean fire = wc.isFire();
        WorldToggleFireEvent event = new WorldToggleFireEvent(p, SystemWorld.getSystemWorld(dc.getWorldname()), fire);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

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
    }

    private void createWorld(Player p, String worldname, File f, File exampleworld, SystemWorld sw) {

        File[] files = f.listFiles();
        for (File file : files) {
            if (file.getName().equals("worldconfig.yml"))
                continue;
            FileUtils.deleteQuietly(file);
        }

        try {
            if (exampleworld.isDirectory())
                FileUtils.copyDirectory(exampleworld, f);
            toConfirm.remove(p);

            FileUtils.moveDirectoryToDirectory(f, Bukkit.getWorldContainer(), false);

            WorldConfig config = WorldConfig.getWorldConfig(worldname);
            config.setHome(null);
            config.save();

            p.sendMessage(MessageConfig.getWorldReseted());

            // For fast worldcreating after reset
            WorldCreator creator = new WorldCreator(worldname);
            long seed = PluginConfig.getSeed();
            World.Environment env = PluginConfig.getEnvironment();
            WorldType type = PluginConfig.getWorldType();
            if (seed != 0)
                creator.seed(seed);
            creator.type(type);
            creator.environment(env);
            String generator = PluginConfig.getGenerator();
            if (!generator.trim().isEmpty())
                creator.generator(generator);

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
