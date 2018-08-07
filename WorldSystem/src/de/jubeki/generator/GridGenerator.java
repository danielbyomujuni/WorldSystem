package de.jubeki.generator;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;

public class GridGenerator extends AbstractGenerator {
	
	public GridGenerator() {
		this(3, 15);
	}
	
	public GridGenerator(int grid) {
		this(3, grid);
	}
	
	private final int height, grid;
	private final byte[][] result = createDefaultResult();
	
	public GridGenerator(int height, int grid) {
		this.height = height < 1 ? 1 : height;
		this.grid = (grid < 1 ? 2 : grid+1);
		setMaterial(result, 1, height-1, Material.DIRT);
		if(grid == 16) {
			for(int i = 0; i < 16; i++) {
				for(int k = 0; k < 16; k++) {
					if(i == 0 || k == 0) {
						setBlock(result, i, height, k, Material.BEDROCK);
					} else {
						setBlock(result, i, height, k, Material.GRASS);
					}
				}
			}
		}
	}
	
	public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomes) {
		setBiome(biomes, Biome.PLAINS);
		if(grid == 16) {
			return result;
		}
		int diffx = 16*x;
		int diffz = 16*z;
		for(int i = 0; i < 16; i++) {
			boolean set = (diffx + i) % grid == 0;
			for(int k = 0; k < 16; k++) {
				if(set || (diffz + k) % grid == 0) {
					setBlock(result, i, height, k, Material.BEDROCK);
				} else {
					setBlock(result, i, height, k, Material.GRASS);
				}
			}
		}
		return result;
	}

}
