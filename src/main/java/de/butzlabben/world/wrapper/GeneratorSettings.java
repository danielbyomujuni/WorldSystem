package de.butzlabben.world.wrapper;


import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class GeneratorSettings {
    final private long seed;
    final private World.Environment environment;
    final private WorldType type;
    final private String generator;

    // Default generatorsettings
    public GeneratorSettings() {
        type = null;
        environment = null;
        seed = 0;
        generator = null;
    }
    public GeneratorSettings(long seed, World.Environment environment, WorldType type,String generator) {
        this.type = type;
        this.environment = environment;
        this.seed = seed;
        this.generator = generator;
    }

    public WorldCreator asWorldCreator(String name) {
        WorldCreator creator = new WorldCreator(name);

        if (type != null)
            creator.type(type);
        if (environment != null)
            creator.environment(environment);
        if (seed != 0)
            creator.seed(seed);
        if (generator != null && !generator.trim().isEmpty())
            creator.generator(generator);

        return creator;
    }
}
