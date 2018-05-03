package de.butzlabben.autoupdater;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.PluginConfig;

/**
 * @author Butzlabben
 * @since 01.05.2018
 */
public class AutoUpdater implements Listener {

	private boolean confirmed;
	private boolean confirmNeed;
	private static AutoUpdater instance;
	private AutoUpdate au;

	public static void startAsync() {
		Thread t = new Thread(() -> {
			getInstance();
		});
		t.setName("update-thread-worldsystem");
		t.start();
	}

	public static synchronized AutoUpdater getInstance() {
		if (instance == null)
			instance = new AutoUpdater(WorldSystem.getInstance());
		return instance;
	}

	private AutoUpdater(JavaPlugin plugin) {
		confirmNeed = PluginConfig.confirmNeed();
		UpdateInformations ui = UpdateInformations.getInformations();
		String v = plugin.getDescription().getVersion();
		if (!ui.getVersion().equals(plugin.getDescription().getVersion())) {
			Bukkit.getConsoleSender().sendMessage(
					PluginConfig.getPrefix() + "Found new version. Current: " + v + ", Available: " + ui.getVersion());

			// Get jar file
			Method getFileMethod = null;
			try {
				getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
			} catch (NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
				return;
			}
			getFileMethod.setAccessible(true);
			File file = null;

			try {
				file = (File) getFileMethod.invoke(plugin);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				return;
			}

			getFileMethod.setAccessible(false);

			String jar = file.getAbsolutePath();
			au = new AutoUpdate(ui, jar);
			if (!confirmNeed) {
				Runtime.getRuntime().addShutdownHook(new Thread(au));
				Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "§aAutoupdate confirmed, §crestart §ato apply changes");
				confirmed = true;
			} else {
				Bukkit.getPluginManager().registerEvents(this, plugin);
				for(Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage(PluginConfig.getPrefix() + "§aFound new update. Confirm autoupdate with §c/ws confirm");
				}
				Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "§aFound new update. Confirm autoupdate with §c/ws confirm");
			}
		}
	}

	@EventHandler
	public void on(PlayerJoinEvent e) {
		if (e.getPlayer().hasPermission("ws.confirm")) {
			e.getPlayer().sendMessage(PluginConfig.getPrefix() + "§aFound new update. Confirm autoupdate with §c/ws confirm");
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
