package de.butzlabben.world.listener;

import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.config.WorldPerm;
import de.butzlabben.world.wrapper.WorldPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorldEditListener implements Listener {

    private final List<String> worldEditCommands= new ArrayList<>();

    public WorldEditListener() {
        try {
            String packageName = Bukkit.getServer().getClass().getPackage().getName();
            String version = packageName.substring(packageName.lastIndexOf(".") + 1);
            Class<?> serverClass = Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer");

            Field f1 = serverClass.getDeclaredField("commandMap");
            f1.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) f1.get(Bukkit.getServer());

            Field f2 = SimpleCommandMap.class.getDeclaredField("knownCommands");
            f2.setAccessible(true);
            @SuppressWarnings({"unchecked", "rawtypes"})
            Map<String, Command> knownCommands = (Map) f2.get(commandMap);
            worldEditCommands.addAll(knownCommands.entrySet().stream().filter(entry -> entry.getKey().contains("worldedit"))
                    .map(entry -> entry.getValue().getName()).collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void playerCommandHandler(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0];
        if (!isWorldEditCommand(command)) {
            return;
        }
        Player p = event.getPlayer();
        String worldname = p.getWorld().getName();
        WorldPlayer wp = new WorldPlayer(p, worldname);
        if (wp.isOnSystemWorld() &&
                !wp.isOwnerofWorld() && !p.hasPermission(WorldPerm.WORLDEDIT.getOpPerm())) {
            WorldConfig wc = WorldConfig.getWorldConfig(p.getWorld().getName());
            if (!wc.hasPermission(p.getUniqueId(), WorldPerm.WORLDEDIT)) {
                p.sendMessage(MessageConfig.getNoPermission());
                event.setCancelled(true);
            }
        }
    }

    private boolean isWorldEditCommand(String command) {
        System.out.println(command);
        return worldEditCommands.contains(command)
                || worldEditCommands.contains(command.replaceFirst("/", ""))
                || worldEditCommands.contains(command.replaceFirst("/worldedit:", ""));
    }
}
