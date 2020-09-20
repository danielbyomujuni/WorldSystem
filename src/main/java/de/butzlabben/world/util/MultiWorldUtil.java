package de.butzlabben.world.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

public class MultiWorldUtil extends FileConfiguration {
    @Override
    public String saveToString() {
        return null;
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {

    }

    @Override
    protected String buildHeader() {
        return null;
    }
}
