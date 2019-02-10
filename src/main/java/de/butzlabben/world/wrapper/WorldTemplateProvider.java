package de.butzlabben.world.wrapper;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;

import de.butzlabben.world.config.PluginConfig;

/**
 * @author Butzlabben
 * @since 16.12.2018
 */
public class WorldTemplateProvider {
	
	private static WorldTemplateProvider instance = new WorldTemplateProvider();
	
	public static WorldTemplateProvider getInstace() {
		return instance;
	}
	
	private HashMap<String, WorldTemplate> templates = new HashMap<>();

	private WorldTemplateProvider() {
		ConfigurationSection section = PluginConfig.getConfig().getConfigurationSection("worldtemplates.templates");
		for(String key: section.getKeys(false)) {
			String name = section.getString(key + ".name");
			templates.put(name, new WorldTemplate(name));
		}
	}
	
	public WorldTemplate getTemplate(String key) {
		return templates.get(key);
	}
	
	public Collection<WorldTemplate> getTemplates() {
		return templates.values();
	}
}
