package de.butzlabben.world.config;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

public class TestPluginConfig {
    @Test
    public void testPluginConfigInit() throws FileNotFoundException {
        File cfgFile = new File("TestFiles/workingDir/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);
    }
}
