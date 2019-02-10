package de.butzlabben.world.gui.worldoption;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.butzlabben.inventory.DependListener;
import de.butzlabben.inventory.OrcItem;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.wrapper.WorldPlayer;

public class FireStatus implements DependListener {

	@Override
	public ItemStack getItemStack(Player p, WorldPlayer wp) {
		String worldname = new DependenceConfig(p).getWorldname();
		File file = new File(worldname + "/worldconfig.yml");
		if (!file.exists())
			file = new File(PluginConfig.getWorlddir() + "/worldconfig.yml");
		if (!file.exists())
			return null;
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		boolean b = cfg.getBoolean("Settings.Fire");
		if (b)
			return OrcItem.enabled.getItemStack(p);

		return null;
		// TODO wenn enabled, dann return OrcItem.enabled.getItemStack(p, wp);
		// sonst return null
	}

}
