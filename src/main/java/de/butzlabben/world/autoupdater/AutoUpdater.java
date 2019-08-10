package de.butzlabben.world.autoupdater;

import de.butzlabben.world.config.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Butzlabben
 * @since 01.05.2018
 */
public class AutoUpdater implements Listener {

    private static AutoUpdater instance;
    private boolean confirmed;
    private boolean confirmNeed;
    private AutoUpdate au;

    private AutoUpdater() {
        confirmNeed = PluginConfig.confirmNeed();
        UpdateInformations ui = UpdateInformations.getInformations();
        if (ui == null) {
            Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "§cCouldn't contact autoupdate server");
            return;
        }
        Plugin plugin = Bukkit.getPluginManager().getPlugin(ui.getPlugin());
        if (plugin == null)
            return;
        String v = plugin.getDescription().getVersion();
        if (!ui.getVersion().equals(plugin.getDescription().getVersion())) {

            if (!ui.isSilent()) {
                Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "Found new version. Current: " + v
                        + ", Available: " + ui.getVersion());
            }
            // Get jar file
            Method getFileMethod;
            try {
                getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            } catch (NoSuchMethodException | SecurityException e1) {
                e1.printStackTrace();
                return;
            }
            getFileMethod.setAccessible(true);
            File file;

            try {
                file = (File) getFileMethod.invoke(plugin);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
                return;
            }

            getFileMethod.setAccessible(false);

            String jar = file.getAbsolutePath();
            au = new AutoUpdate(ui, jar);
            if (ui.isSilent() || !confirmNeed) {
                Runtime.getRuntime().addShutdownHook(new Thread(au));
                if (!ui.isSilent())
                    Bukkit.getConsoleSender().sendMessage(
                            PluginConfig.getPrefix() + "§aAutoupdate confirmed, §crestart §ato apply changes");
                confirmed = true;
            } else {
                Bukkit.getPluginManager().registerEvents(this, plugin);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(
                            PluginConfig.getPrefix() + "§aFound new update. Confirm autoupdate with §c/ws confirm");
                    p.sendMessage(PluginConfig.getPrefix() + "§aRead changelogs: https://www.spigotmc.org/resources/49756/updates");
                }
                Bukkit.getConsoleSender().sendMessage(
                        PluginConfig.getPrefix() + "§aFound new update. Confirm autoupdate with §c/ws confirm");
                Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "§aRead changelogs: https://www.spigotmc.org/resources/49756/updates");
            }
        } else {
            confirmNeed = false;
        }
    }

    public static void startAsync() {
        Thread t = new Thread(() -> {
            getInstance();
        });
        t.setName("update-thread-worldsystem");
        t.start();
    }

    public static synchronized AutoUpdater getInstance() {
        if (instance == null)
            instance = new AutoUpdater();
        return instance;
    }

    @EventHandler
    public void on(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("ws.confirm")) {
            e.getPlayer().sendMessage(
                    PluginConfig.getPrefix() + "§aFound new update. Confirm autoupdate with §c/ws confirm");
            e.getPlayer().sendMessage(PluginConfig.getPrefix() + "§aRead changelogs: https://www.spigotmc.org/resources/49756/updates");
        }
    }

    public boolean confirm() {
        if (confirmNeed && !confirmed) {
            Runtime.getRuntime().addShutdownHook(new Thread(au));
            confirmed = true;
            HandlerList.unregisterAll(this);
            return true;
        }
        return false;
    }

    public boolean confirmed() {
        return confirmed;
    }

}
