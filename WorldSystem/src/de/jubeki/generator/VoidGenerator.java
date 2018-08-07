package de.jubeki.generator;

import java.util.Random;

import org.bukkit.World;

public class VoidGenerator extends AbstractGenerator {
	
	private final byte[][] result = new byte[16][];
	
	public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomes) {
//		setBiome(biomes, Biome.VOID);
		return result;
	}
	
}
