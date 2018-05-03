package de.jubeki.generator;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

abstract class AbstractGenerator extends ChunkGenerator {

	void checkHeight(byte[][] result, int y) {
		if (result[y >> 4] == null) {
			result[y >> 4] = new byte[4096];
		}
	}
	
	@SuppressWarnings("deprecation")
	void setBlock(byte[][] result, int x, int y, int z, Material mat) {
		result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = (byte) mat.getId();
	}

	byte[][] createDefaultResult() {
		byte[][] result = new byte[16][];
		checkHeight(result, 0);
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				setBlock(result, x, 0, z, Material.BEDROCK);
			}
		}
		return result;
	}

	void setMaterial(byte[][] result, int min, int max, Material mat) {
		for (int y = min; y <= max; y++) {
			checkHeight(result, y);
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					setBlock(result, x, 0, z, mat);
				}
			}
		}
	}

	void setBiome(BiomeGrid biomes, Biome biome) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				biomes.setBiome(x, z, biome);
			}
		}
	}
}
