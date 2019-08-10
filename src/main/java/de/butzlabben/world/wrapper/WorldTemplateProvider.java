package de.butzlabben.world.wrapper;

import de.butzlabben.world.config.PluginConfig;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author Butzlabben
 * @since 16.12.2018
 */
public class WorldTemplateProvider {

    private static final WorldTemplateProvider instance = new WorldTemplateProvider();
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

            GeneratorSettings settings = new GeneratorSettings();
            if (section.contains(key + ".generator")) {
                ConfigurationSection gSection = section.getConfigurationSection(key + ".generator");
                long seed = gSection.getLong("seed", 0);
                String env = gSection.getString("environment");
                String type = gSection.getString("type");
                String generator = gSection.getString("generator");
                settings = new GeneratorSettings(seed, getEnvironment(env), getWorldType(type), generator);
            }

            templates.put(name, new WorldTemplate(name, permission, cost, settings));
        }
    }

    public static WorldTemplateProvider getInstance() {
        return instance;
    }

    public WorldTemplate getTemplate(String key) {
        return templates.get(key);
    }

    public Collection<WorldTemplate> getTemplates() {
        return templates.values();
    }

    private World.Environment getEnvironment(String env) {
        if (env == null)
            return null;
        try {
            return World.Environment.valueOf(env);
        } catch (Exception ignored) {
        }
        return null;
    }

    private WorldType getWorldType(String type) {
        if (type == null)
            return null;
        try {
            return WorldType.valueOf(type);
        } catch (Exception ignored) {
        }
        return null;
    }
}
