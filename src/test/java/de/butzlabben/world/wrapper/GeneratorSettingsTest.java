package de.butzlabben.world.wrapper;

import org.bukkit.WorldCreator;
import org.junit.Test;

import static org.junit.Assert.*;

public class GeneratorSettingsTest {

    @Test
    public void asWorldCreator() {
        GeneratorSettings settings = new GeneratorSettings();
        WorldCreator creator = settings.asWorldCreator("test");
    }
}