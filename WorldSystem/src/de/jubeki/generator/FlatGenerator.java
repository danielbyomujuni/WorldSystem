package de.jubeki.generator;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;

public class FlatGenerator extends AbstractGenerator {

	private final byte[][] result = createDefaultResult();
	
	public FlatGenerator() {
		this(3);
	}
	
	public FlatGenerator(int height) {
		if(height < 1) {
			height = 1;
		}
		setDirt(height);
		setGrass(height);
	}
	
	@Override
	public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomes) {
		setBiome(biomes, Biome.PLAINS);
		return result;
	}
	
	private void setGrass(int height) {
		checkHeight(result, height);
		for(int x = 0; x < 16; x++) {
			for(int z = 0; z < 16; z++) {
				setBlock(result, x, height, z, Material.GRASS);
			}
		}
	}
	
	private void setDirt(int height) {
		for(int y = 1; y < height; y++) {
			checkHeight(result, y);
			for(int x = 0; x < 16; x++) {
				for(int z = 0; z < 16; z++) {
					setBlock(result, x, y, z, Material.DIRT);
				}
			}
		}
	}
	
	

}
