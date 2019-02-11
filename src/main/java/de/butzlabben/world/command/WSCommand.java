package de.butzlabben.world.command;

import de.butzlabben.autoupdater.AutoUpdater;
import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.gui.WorldChooseGUI;
import de.butzlabben.world.gui.WorldSystemGUI;
import de.butzlabben.world.wrapper.SystemWorld;
import de.butzlabben.world.wrapper.WorldPlayer;
import de.butzlabben.world.wrapper.WorldTemplate;
import de.butzlabben.world.wrapper.WorldTemplateProvider;
import net.myplayplanet.commandframework.CommandArgs;
import net.myplayplanet.commandframework.api.Command;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class WSCommand {

    @Command(name = "ws")
    public void mainCommand(CommandArgs args) {
        CommandSender cs = args.getSender(CommandSender.class);

        String prefix = PluginConfig.getPrefix();
        cs.sendMessage(
                prefix + "WorldSystem by Butzlabben v" + WorldSystem.getInstance().getDescription().getVersion());
        cs.sendMessage(prefix + "Contributor: Jubeki");
        List<String> cmdHelp = MessageConfig.getCommandHelp();
        cmdHelp.forEach(s -> cs.sendMessage(prefix + s));
        if (cs.hasPermission("ws.delete"))
            cs.sendMessage(MessageConfig.getDeleteCommandHelp());
    }

    @Command(name ="ws.gui", inGameOnly = true)
    public void guiCommand(CommandArgs args) {
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
        p.openInventory(new WorldSystemGUI().getInventory(p));
    }

    @Command(name = "ws.confirm", permission = "ws.confirm")
    public void confirmCommand(CommandArgs args) {
        CommandSender cs = args.getSender(Player.class);

        if (AutoUpdater.getInstance().confirmed()) {
            cs.sendMessage(PluginConfig.getPrefix() + "§cAlready confirmed or no confirm needed");
            return;
        }
        AutoUpdater.getInstance().confirm();
        cs.sendMessage(PluginConfig.getPrefix() + "§aAutoupdate confirmed, §crestart §ato apply changes");
    }

    @Command(name = "ws.get", inGameOnly = true)
    public void getCommand(CommandArgs args) {
        Player p = args.getSender(Player.class);
        // create New Entry
        DependenceConfig dc = new DependenceConfig(p);
        if (dc.hasWorld()) {
            p.sendMessage(MessageConfig.getWorldAlreadyExists());
            return;
        }

        if(!p.hasPermission("ws.get")) {
            p.sendMessage(MessageConfig.getNoPermission());
            return;
        }

        if (PluginConfig.isMultiChoose()) {
            if (args.length() > 0) {
                String key = args.getArgument(0);
                WorldTemplate template = WorldTemplateProvider.getInstace().getTemplate(key);
                if (template != null) {
                    create(p, template);
                    return;
                }
            }
            p.openInventory(new WorldChooseGUI().getInventory(p));
        } else {
            WorldTemplate template = WorldTemplateProvider.getInstace()
                    .getTemplate(PluginConfig.getDefaultWorldTemplate());
            if (template != null)
                create(p, template);
            else {
                p.sendMessage(PluginConfig.getPrefix() + "§cError in config at \"worldtemplates.default\"");
                p.sendMessage(PluginConfig.getPrefix() + "§cPlease contact an administrator");
            }
        }
    }

    @Command(name = "ws.home", inGameOnly = true)
    public void homeCommand(CommandArgs args) {
        Player p = args.getSender(Player.class);

        String worldname = p.getWorld().getName();
        DependenceConfig dc = new DependenceConfig(p);
        if (!dc.hasWorld()) {
            p.sendMessage(MessageConfig.getNoWorldOwn());
            return;
        }
        WorldPlayer wp = new WorldPlayer(p, worldname);
        if (wp.isOnSystemWorld()) {
            SystemWorld.tryUnloadLater(Bukkit.getWorld(worldname));
        }
        SystemWorld sw = SystemWorld.getSystemWorld(dc.getWorldname());
        if(sw == null) {
            p.sendMessage(MessageConfig.getNoWorldOwn());
            return;
        }
        if (sw.isLoaded()) {
            sw.teleportToWorldSpawn(p);
        } else {
            sw.load(p);
        }
    }

    @Command(name = "ws.info", inGameOnly = true)
    public void infoComannd(CommandArgs args) {
        Player p = args.getSender(Player.class);

        WorldPlayer wp = new WorldPlayer(p, p.getWorld().getName());
        if (!wp.isOnSystemWorld()) {
            p.sendMessage(MessageConfig.getNotOnWorld());
            return;
        }

        WorldConfig wc = WorldConfig.getWorldConfig(p.getWorld().getName());
        int id = wc.getId();
        String owner = wc.getOwnerName();
        boolean fire = wc.isFire();
        boolean tnt = wc.isTnt();

        p.sendMessage(MessageConfig.getInfoOwner().replaceAll("%data", owner));
        p.sendMessage(MessageConfig.getInfoId().replaceAll("%data", "" + id));
        p.sendMessage(MessageConfig.getInfoTnt().replaceAll("%data",
                tnt ? MessageConfig.getInfoEnabled() : MessageConfig.getInfoDisabled()));
        p.sendMessage(MessageConfig.getInfoFire().replaceAll("%data",
                fire ? MessageConfig.getInfoEnabled() : MessageConfig.getInfoDisabled()));
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = wc.getMembersWithNames().values().iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if(it.hasNext())
                sb.append(" ");
        }
        p.sendMessage(MessageConfig.getInfoMember().replaceAll("%data", sb.toString().trim()));
    }

    @Command(name = "ws.leave", inGameOnly = true)
    public void leaveCommand(CommandArgs args) {
        Player p = args.getSender(Player.class);

        String worldname = p.getWorld().getName();
        WorldPlayer wp = new WorldPlayer(p, worldname);

        if (wp.isOnSystemWorld()) {
            // Extra safety for #2
            if (PluginConfig.getSpawn().getWorld() == null) {
                Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "§cThe spawn is not properly set");
                p.sendMessage(PluginConfig.getPrefix() + "§cThe spawn is not properly set");
                return;
            }

            p.teleport(PluginConfig.getSpawn());
            p.setGameMode(PluginConfig.getSpawnGamemode());
            World w = Bukkit.getWorld(p.getWorld().getName());
            SystemWorld.tryUnloadLater(w);
        } else {
            p.sendMessage(MessageConfig.getNotOnWorld());
        }
    }

    @Command(name = "ws.tp", inGameOnly = true)
    public void tpCommand(CommandArgs args) {
        Player p = args.getSender(Player.class);

        if (args.length() < 1) {
            p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws tp <World>"));
            return;
        }

        if(args.getArgument(0).equalsIgnoreCase(p.getName()) || args.getArgument(0).equalsIgnoreCase(p.getUniqueId().toString())) {
            p.chat("/ws home");
            return;
        }


        DependenceConfig dc = new DependenceConfig(args.getArgument(0));
        String worldname = dc.getWorldNamebyOfflinePlayer();
        if (!dc.hasWorld()) {
            p.sendMessage(MessageConfig.getNoWorldOther());
            return;
        }
        SystemWorld sw = SystemWorld.getSystemWorld(worldname);
        WorldPlayer wp1 = new WorldPlayer(p, p.getWorld().getName());
        WorldPlayer wp = new WorldPlayer(p, worldname);
        if (p.getWorld().getName().equals(worldname)) {
            sw.teleportToWorldSpawn(p);
            return;
        }
        if (!p.hasPermission("ws.tp.world")) {
            if (!wp.isMemberofWorld(worldname) && !wp.isOwnerofWorld()) {
                p.sendMessage(MessageConfig.getNoMemberOther());
                return;
            }
        }
        if (wp1.isOnSystemWorld()) {
            World w = p.getWorld();
            SystemWorld.tryUnloadLater(w);
        }
        if (sw != null)
            if (!sw.isLoaded()) {
                sw.load(p);
            } else {
                sw.teleportToWorldSpawn(p);
            }
    }

    private void create(Player p, WorldTemplate template) {
        Bukkit.getScheduler().runTask(WorldSystem.getInstance(), () -> {
            if (SystemWorld.create(p, template))
                p.sendMessage(MessageConfig.getSettingUpWorld());
        });
    }
}