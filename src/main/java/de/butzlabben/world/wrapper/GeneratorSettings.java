package de.butzlabben.world.wrapper;

import lombok.AllArgsConstructor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

@AllArgsConstructor
public class GeneratorSettings {
    final private long seed;
    final private World.Environment environment;
    final private WorldType type;
    final private String generator;

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
