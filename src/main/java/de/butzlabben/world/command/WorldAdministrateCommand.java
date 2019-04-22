package de.butzlabben.world.command;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.event.WorldAddmemberEvent;
import de.butzlabben.world.event.WorldDeleteEvent;
import de.butzlabben.world.event.WorldRemovememberEvent;
import de.butzlabben.world.util.PlayerPositions;
import de.butzlabben.world.wrapper.SystemWorld;
import de.butzlabben.world.wrapper.WorldPlayer;
import net.myplayplanet.commandframework.CommandArgs;
import net.myplayplanet.commandframework.api.Command;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class WorldAdministrateCommand {

    @Command(name = "ws.delmember", inGameOnly = true, usage = "/ws delmember <Player>")
    public void delMemberCommand(CommandArgs args) {
        Player p = args.getSender(Player.class);

        if (args.length() < 1) {
            p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws delmember <Player>"));
            return;
        }

        DependenceConfig dc = new DependenceConfig(p);
        if (!dc.hasWorld()) {
            p.sendMessage(MessageConfig.getNoWorldOwn());
            return;
        }

        @SuppressWarnings("deprecation")
        OfflinePlayer a = Bukkit.getOfflinePlayer(args.getArgument(0));
        WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname());
        if (a == null) {
            p.sendMessage(MessageConfig.getNotRegistered().replaceAll("%player", args.getArgument(0)));
            return;
        } else if (!wc.isMember(a.getUniqueId())) {
            p.sendMessage(MessageConfig.getNoMemberOwn());
            return;
        }
        WorldRemovememberEvent event = new WorldRemovememberEvent(a.getUniqueId(), dc.getWorldname(), p);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

        wc.removeMember(a.getUniqueId());
        if (a.isOnline()) {
            Player t = (Player) a;
            if (t.getWorld().getName().equals(new DependenceConfig(p).getWorldname())) {
                t.teleport(PluginConfig.getSpawn());
                t.setGameMode(PluginConfig.getSpawnGamemode());
            }
        }
        try {
            wc.save();
        } catch (IOException e) {
            p.sendMessage(MessageConfig.getUnknownError());
            e.printStackTrace();
        }
        p.sendMessage(MessageConfig.getMemberRemoved().replaceAll("%player", a.getName()));
    }

    @Command(name = "ws.delete", permission = "ws.delete", usage = "/ws delete <Player>")
    public void deleteCommand(CommandArgs args) {
        CommandSender cs = args.getSender(CommandSender.class);

        if (args.length() < 1) {
            cs.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws delete <Player>"));
            return;
        }

        DependenceConfig dc = new DependenceConfig(args.getArgument(0));
        if (!dc.hasWorld()) {
            cs.sendMessage(MessageConfig.getNoWorldOther());
            return;
        }

        String worldname = dc.getWorldname();
        SystemWorld sw = SystemWorld.getSystemWorld(worldname);
        WorldDeleteEvent event = new WorldDeleteEvent(cs, sw);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

        if (sw != null && sw.isLoaded())
            sw.directUnload(Bukkit.getWorld(worldname));

        WorldConfig config = WorldConfig.getWorldConfig(worldname);
        // Delete unnecessary positions
        PlayerPositions.getInstance().deletePositions(config);

        Bukkit.getScheduler().runTaskLater(WorldSystem.getInstance(), () -> {
            OfflinePlayer op = dc.getOwner();

            String uuid = op.getUniqueId().toString();
            File dir = new File(PluginConfig.getWorlddir() + "/" + worldname);
            if (!dir.exists())
                dir = new File(Bukkit.getWorldContainer(), worldname);
            if (dir.exists())
                try {
                    FileUtils.deleteDirectory(dir);
                } catch (Exception e) {
                    cs.sendMessage(MessageConfig.getUnknownError());
                    e.printStackTrace();
                }
            File dconfig = new File("plugins//WorldSystem//dependence.yml");
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
            cfg.set("Dependences." + uuid + ".ID", null);
            cfg.set("Dependences." + uuid + ".ActualName", null);
            cfg.set("Dependences." + uuid, null);
            try {
                cfg.save(dconfig);
            } catch (Exception e) {
                cs.sendMessage(MessageConfig.getUnknownError());
                e.printStackTrace();
            }
            cs.sendMessage(MessageConfig.getDeleteWorldOther().replaceAll("%player", op.getName()));
            if (op.isOnline()) {
                Player p1 = Bukkit.getPlayer(op.getUniqueId());
                p1.sendMessage(MessageConfig.getDeleteWorldOwn());
            }
        }, 10);
    }

    @Command(name = "ws.addmember", inGameOnly = true, usage = "/ws addmember <Player>")
    public void addMemberCommand(CommandArgs args) {
        Player p = args.getSender(Player.class);

        if (args.length() < 1) {
            p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws addmember <Player>"));
            return;
        }

        DependenceConfig dc = new DependenceConfig(p);
        if (!dc.hasWorld()) {
            p.sendMessage(MessageConfig.getNoWorldOwn());
            return;
        }
        @SuppressWarnings("deprecation")
        OfflinePlayer a = Bukkit.getOfflinePlayer(args.getArgument(0));
        WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname());
        if (a == null) {
            p.sendMessage(MessageConfig.getNotRegistered().replaceAll("%player", args.getArgument(0)));
            return;
        } else if (wc.isMember(a.getUniqueId())) {
            p.sendMessage(MessageConfig.getAlreadyMember());
            return;
        }

        WorldAddmemberEvent event = new WorldAddmemberEvent(a.getUniqueId(), dc.getWorldname(), p);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

        wc.addMember(a.getUniqueId());
        try {
            wc.save();
        } catch (IOException e) {
            p.sendMessage(MessageConfig.getUnknownError());
            e.printStackTrace();
        }
        p.sendMessage(MessageConfig.getMemberAdded().replaceAll("%player", a.getName()));
    }

    @Command(name = "ws.toggletp", inGameOnly = true, usage = "/ws toggletp <Player>")
    public void toggleTeleportCommand(CommandArgs args) {
        Player p = args.getSender(Player.class);

        if (args.length() < 1) {
            p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws toggletp <Player>"));
            return;
        }

        DependenceConfig dc = new DependenceConfig(p);
        if (!dc.hasWorld()) {
            p.sendMessage(MessageConfig.getNoWorldOwn());
            return;
        }
        @SuppressWarnings("deprecation")
        OfflinePlayer a = Bukkit.getOfflinePlayer(args.getArgument(0));
        WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname());
        if (!wc.isMember(a.getUniqueId())) {
            p.sendMessage(MessageConfig.getNoMemberOwn());
            return;
        }
        WorldPlayer wp = new WorldPlayer(a, dc.getWorldname());
        if (wp.isOwnerofWorld()) {
            p.sendMessage(PluginConfig.getPrefix() + "§cYou are the owner");
            return;
        }
        if (wp.toggleTeleport()) {
            p.sendMessage(MessageConfig.getToggleTeleportEnabled().replaceAll("%player", a.getName()));
        } else {
            p.sendMessage(MessageConfig.getToggleTeleportDisabled().replaceAll("%player", a.getName()));
        }
    }

    @Command(name = "ws.togglegm", inGameOnly = true, usage = "/ws togglegm <Player>")
    public void toggleGamemodeCommand(CommandArgs args) {
        Player p = args.getSender(Player.class);

        if (args.length() < 1) {
            p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws togglegm <Player>"));
            return;
        }

        DependenceConfig dc = new DependenceConfig(p);
        if (!dc.hasWorld()) {
            p.sendMessage(MessageConfig.getNoWorldOwn());
            return;
        }
        @SuppressWarnings("deprecation")
        OfflinePlayer a = Bukkit.getOfflinePlayer(args.getArgument(0));
        WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname());
        if (!wc.isMember(a.getUniqueId())) {
            p.sendMessage(MessageConfig.getNoMemberOwn());
            return;
        }
        WorldPlayer wp = new WorldPlayer(a, dc.getWorldname());
        if (wp.isOwnerofWorld()) {
            p.sendMessage(PluginConfig.getPrefix() + "§cYou are the owner");
            return;
        }
        if (wp.toggleGamemode()) {
            p.sendMessage(MessageConfig.getToggleGameModeEnabled().replaceAll("%player", a.getName()));
        } else {
            p.sendMessage(MessageConfig.getToggleGameModeDisabled().replaceAll("%player", a.getName()));
        }
    }

    @Command(name = "ws.togglewe", inGameOnly = true, usage = "/ws togglewe <Player>")
    public void toggleWorldeditCommand(CommandArgs args) {
        Player p = args.getSender(Player.class);

        if (args.length() < 1) {
            p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws togglewe <Player>"));
            return;
        }

        DependenceConfig dc = new DependenceConfig(p);
        if (!dc.hasWorld()) {
            p.sendMessage(MessageConfig.getNoWorldOwn());
            return;
        }
        @SuppressWarnings("deprecation")
        OfflinePlayer a = Bukkit.getOfflinePlayer(args.getArgument(0));
        WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname());
        if (!wc.isMember(a.getUniqueId())) {
            p.sendMessage(MessageConfig.getNoMemberOwn());
            return;
        }
        WorldPlayer wp = new WorldPlayer(a, dc.getWorldname());
        if (wp.isOwnerofWorld()) {
            p.sendMessage(PluginConfig.getPrefix() + "§cYou are the owner");
            return;
        }
        if (wp.toggleWorldEdit()) {
            p.sendMessage(MessageConfig.getToggleWorldeditEnabled().replaceAll("%player", a.getName()));
        } else {
            p.sendMessage(MessageConfig.getToggleWorldeditDisabled().replaceAll("%player", a.getName()));
        }
    }

    @Command(name = "ws.togglebuild", inGameOnly = true, usage = "/ws togglebuild <Player>")
    public void toggleBuildCommand(CommandArgs args) {
        Player p = args.getSender(Player.class);

        if (args.length() < 1) {
            p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws togglebuild <Player>"));
            return;
        }

        DependenceConfig dc = new DependenceConfig(p);
        if (!dc.hasWorld()) {
            p.sendMessage(MessageConfig.getNoWorldOwn());
            return;
        }
        @SuppressWarnings("deprecation")
        OfflinePlayer a = Bukkit.getOfflinePlayer(args.getArgument(0));
        WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname());
        if (!wc.isMember(a.getUniqueId())) {
            p.sendMessage(MessageConfig.getNoMemberOwn());
            return;
        }
        WorldPlayer wp = new WorldPlayer(a, dc.getWorldname());
        if (wp.isOwnerofWorld()) {
            p.sendMessage(PluginConfig.getPrefix() + "§cYou are the owner");
            return;
        }
        if (wp.toggleBuild()) {
            p.sendMessage(MessageConfig.getToggleBuildEnabled().replaceAll("%player", a.getName()));
        } else {
            p.sendMessage(MessageConfig.getToggleBuildDisabled().replaceAll("%player", a.getName()));
        }
    }
}
