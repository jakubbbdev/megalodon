package dev.jakub.megalodon.dev.jakub.megalodon.world.generator

import org.bukkit.Material
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo
import java.util.Random

class ChessGenerator : ChunkGenerator() {

    override fun generateBedrock(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int, chunkData: ChunkData) {
        val isEven = (chunkX + chunkZ) % 2 == 0
        val topBlock = if (isEven) Material.LIGHT_GRAY_CONCRETE else Material.GRAY_CONCRETE

        for (x in 0 until 16) {
            for (z in 0 until 16) {
                for (y in 0..63) {
                    chunkData.setBlock(x, y, z, Material.STONE)
                }
                chunkData.setBlock(x, 64, z, topBlock)
            }
        }
    }
}