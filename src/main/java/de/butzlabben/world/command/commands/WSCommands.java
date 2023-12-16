package de.butzlabben.world.command.commands;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.gui.WorldChooseGUI;
import de.butzlabben.world.gui.WorldSystemGUI;
import de.butzlabben.world.util.MoneyUtil;
import de.butzlabben.world.wrapper.SystemWorld;
import de.butzlabben.world.wrapper.WorldPlayer;
import de.butzlabben.world.wrapper.WorldTemplate;
import de.butzlabben.world.wrapper.WorldTemplateProvider;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class WSCommands {
    public boolean mainCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandSender cs = sender;

        String prefix = PluginConfig.getPrefix();
        cs.sendMessage(
                prefix + "WorldSystem by CrazyCloudCraft v" + WorldSystem.getInstance().getDescription().getVersion());
        cs.sendMessage(prefix + "Contributors: Jubeki, montlikadani, jstoeckm2, Butzlabben");
        List<String> cmdHelp = MessageConfig.getCommandHelp();
        cmdHelp.forEach(s -> cs.sendMessage("§6" + s)); //(prefix + s));
        // cs.sendMessage(prefix + "==============");
        if (cs.hasPermission("ws.delete")) {
            cs.sendMessage("§6" + MessageConfig.getDeleteCommandHelp());
        }
        return true;
    }

    public boolean guiCommand(CommandSender sender, Command command, String label, String[] args) {
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
            p.openInventory(new WorldSystemGUI().getInventory(p));
            return true;
        } else {
            sender.sendMessage("No Console"); //TODO Get Config
            return false;
        }
    }

    /*public boolean confirmCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandSender cs = sender;

        if (AutoUpdater.getInstance().confirmed()) {
            cs.sendMessage(PluginConfig.getPrefix() + "§cAlready confirmed or no confirm needed");
            return false;
        }
        AutoUpdater.getInstance().confirm();
        cs.sendMessage(PluginConfig.getPrefix() + "§aAutoupdate confirmed, §crestart §ato apply changes");
        return true;
    }*/


    public boolean getCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;

            // Check if use can create a world
            if (!p.hasPermission("ws.get")) {
                p.sendMessage(MessageConfig.getNoPermission());
                return false;
            }

            // create New Entry
            DependenceConfig dc = new DependenceConfig(p);
            if (dc.hasWorld()) {
                p.sendMessage(MessageConfig.getWorldAlreadyExists());
                return false;
            }


            if (PluginConfig.isMultiChoose()) {
                if (args.length > 1) {
                    String key = args[1];
                    WorldTemplate template = WorldTemplateProvider.getInstance().getTemplate(key);
                    if (template != null) {
                        // Permission for this specific template
                        if (template.getPermission() != null && !p.hasPermission(template.getPermission())) {
                            p.sendMessage(MessageConfig.getNoPermission());
                            return false;
                        }

                        // Implementation check for #15
                        if (template.getCost() > 0) {
                            if (!MoneyUtil.hasMoney(p.getUniqueId(), template.getCost())) {
                                p.sendMessage(MessageConfig.getNotEnoughMoney());
                                return false;
                            }
                            MoneyUtil.removeMoney(p.getUniqueId(), template.getCost());
                        }

                        this.create(p, template);
                        return false;
                    }
                }
                WorldChooseGUI.letChoose(p);
            } else {
                WorldTemplate template = WorldTemplateProvider.getInstance()
                        .getTemplate(PluginConfig.getDefaultWorldTemplate());
                if (template != null)
                    this.create(p, template);
                else {
                    p.sendMessage(PluginConfig.getPrefix() + "§cError in config at \"worldtemplates.default\"");
                    p.sendMessage(PluginConfig.getPrefix() + "§cPlease contact an administrator");
                }
            }
            return true;
        } else {
            sender.sendMessage("No Console"); //TODO Add Config
            return false;
        }

    }
    public boolean homeCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            String worldname = p.getWorld().getName();
            DependenceConfig dc = new DependenceConfig(p);
            if (!dc.hasWorld()) {
                p.sendMessage(MessageConfig.getNoWorldOwn());
                return false;
            }
            WorldPlayer wp = new WorldPlayer(p, worldname);
            if (wp.isOnSystemWorld()) {
                SystemWorld.tryUnloadLater(Bukkit.getWorld(worldname));
            }
            SystemWorld sw = SystemWorld.getSystemWorld(dc.getWorldname());
            if (sw == null) {
                p.sendMessage(MessageConfig.getNoWorldOwn());
                return false;
            }
            if (sw.isLoaded()) {
                sw.teleportToWorldSpawn(p);
            } else {
                sw.load(p);
            }
            return true;
        } else {
            sender.sendMessage("No Console"); //TODO Add Config
            return false;
        }
    }

    public boolean infoComannd(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            // Player p = (Player) sender;
            WorldPlayer wp = new WorldPlayer(p, p.getWorld().getName());
            if (!wp.isOnSystemWorld()) {
                p.sendMessage(MessageConfig.getNotOnWorld());
                return false;
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
                if (it.hasNext())
                    sb.append(" ");
            }
            p.sendMessage(MessageConfig.getInfoMember().replaceAll("%data", sb.toString().trim()));
            return true;
        } else {
            sender.sendMessage("No Console"); //TODO Get Config
            return false;
        }
    }

    public boolean leaveCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
        String worldname = p.getWorld().getName();
        WorldPlayer wp = new WorldPlayer(p, worldname);

        if (wp.isOnSystemWorld()) {
            // Extra safety for #2
            if (PluginConfig.getSpawn(null).getWorld() == null) {
                Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "§cThe spawn is not properly set");
                p.sendMessage(PluginConfig.getPrefix() + "§cThe spawn is not properly set");
                return false;
            }

            p.teleport(PluginConfig.getSpawn(p));
            p.setGameMode(GameMode.SURVIVAL); //p.setGameMode(PluginConfig.getSpawnGamemode());
            World w = Bukkit.getWorld(p.getWorld().getName());
            SystemWorld.tryUnloadLater(w);
            } else {
                p.sendMessage(MessageConfig.getNotOnWorld());
            }
            return true;
        } else {
            sender.sendMessage("No Console"); //TODO Get Config
            return false;
        }
    }


    public boolean tpCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length < 2) {
                p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws tp <World>"));
                return false;
            }

            if (args[1].equalsIgnoreCase(p.getName()) || args[1].equalsIgnoreCase(p.getUniqueId().toString())) {
                p.chat("/ws home");
                return false;
            }


            DependenceConfig dc = new DependenceConfig(args[1]);
            String worldname = dc.getWorldNameByOfflinePlayer();
            if (!dc.hasWorld()) {
                p.sendMessage(MessageConfig.getNoWorldOther());
                return false;
            }
            SystemWorld sw = SystemWorld.getSystemWorld(worldname);
            WorldPlayer wp1 = new WorldPlayer(p, p.getWorld().getName());
            WorldPlayer wp = new WorldPlayer(p, worldname);
            if (p.getWorld().getName().equals(worldname)) {
                sw.teleportToWorldSpawn(p);
                return false;
            }
            if (!p.hasPermission("ws.tp.world")) {
                if (!wp.isMemberofWorld(worldname) && !wp.isOwnerofWorld()) {
                    p.sendMessage(MessageConfig.getNoMemberOther());
                    return false;
                }
            }
            if (wp1.isOnSystemWorld()) {
                World w = p.getWorld();
                SystemWorld.tryUnloadLater(w);
            }
            if (sw != null) {
                if (!sw.isLoaded()) {
                    sw.load(p);
                } else {
                    sw.teleportToWorldSpawn(p);
                }
            }
            return true;
        } else {
            sender.sendMessage("No Console"); //TODO Get Config
            return false;
        }
    }


    private void create(Player p, WorldTemplate template) {
        Bukkit.getScheduler().runTask(WorldSystem.getInstance(), () -> {
            if (SystemWorld.create(p, template))
                p.sendMessage(MessageConfig.getSettingUpWorld());
        });
    }
}
