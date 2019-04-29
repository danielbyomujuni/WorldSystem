package de.butzlabben.world.wrapper;

import de.butzlabben.world.config.PluginConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author Butzlabben
 * @since 16.12.2018
 */
public class WorldTemplateProvider {

    private static final WorldTemplateProvider instance = new WorldTemplateProvider();

    public static WorldTemplateProvider getInstace() {
        return instance;
    }

    private final HashMap<String, WorldTemplate> templates = new HashMap<>();

    private WorldTemplateProvider() {
        ConfigurationSection section = PluginConfig.getConfig().getConfigurationSection("worldtemplates.templates");
        for (String key : section.getKeys(false)) {
            String name = section.getString(key + ".name");
            String permission = null;
            if (section.isString(key + ".permission"))
                 permission = section.getString(key + ".permission");

            int cost = -1;
            // Get money for #15 if needed
            if (section.isInt(key + ".cost"))
                cost = section.getInt(key + ".cost");

            templates.put(name, new WorldTemplate(name, permission, cost));
        }
    }

    public WorldTemplate getTemplate(String key) {
        return templates.get(key);
    }

    public Collection<WorldTemplate> getTemplates() {
        return templates.values();
    }
}
