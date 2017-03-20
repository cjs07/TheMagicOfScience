package com.deepwelldevelopment.tmos.common.lib.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

import static com.deepwelldevelopment.tmos.api.blocks.TMOSBlocks.*;

public class TMOSWorldGen implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == 0) { // the overworld
            generateOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
        }
    }

    private void generateOverworld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        generateOre(oreCopper.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 64, 4 + random.nextInt(5), 8);
        generateOre(oreTin.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 64, 4 + random.nextInt(5), 7);
        generateOre(oreAluminum.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 64, 4 + random.nextInt(5), 6);
        generateOre(oreLead.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 32, 4 + random.nextInt(5), 5);
        generateOre(oreSilver.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 32, 4 + random.nextInt(5), 5);
        generateOre(oreNickel.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 32, 2 + random.nextInt(5), 4);
        generateOre(orePlatinum.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 32, 4 + random.nextInt(5), 3);
        generateOre(oreIridium.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 5, 15, 1 + random.nextInt(2), 1);
        generateOre(oreChromium.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 64, 4 + random.nextInt(5), 6);
    }

    private void generateOre(IBlockState ore, World world, Random random, int x, int z, int minY, int maxY, int size, int chances) {
        int deltaY = maxY - minY;

        for (int i = 0; i < chances; i++) {
            BlockPos pos = new BlockPos(x + random.nextInt(16), minY + random.nextInt(deltaY), z + random.nextInt(16));

            WorldGenMinable generator = new WorldGenMinable(ore, size);
            generator.generate(world, random, pos);
        }
    }
}
