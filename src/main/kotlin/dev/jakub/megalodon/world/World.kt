package dev.jakub.megalodon.dev.jakub.megalodon.world

import dev.jakub.megalodon.dev.jakub.megalodon.world.data.WorldBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.world.data.WorldGenerationTypes
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import java.io.File

fun createWorld(
    name: String,
    generationTypes: WorldGenerationTypes = WorldGenerationTypes.NORMAL,
    environment: World.Environment = World.Environment.NORMAL,
) = WorldBuilder(name, generationTypes, environment, false).build()

fun createBuildingWorld(
    name: String,
) = WorldBuilder(name, WorldGenerationTypes.VOID, World.Environment.NORMAL, true).build()

fun File.isWorldContainer(): Boolean {
    val lockFile = File(this, "session.lock")
    return lockFile.exists()
}

fun String.isWorldContainer(): Boolean {
    val possibleWorldContainers = Bukkit.getWorldContainer().listFiles() ?: return false

    for (file in possibleWorldContainers) {
        if (!file.isWorldContainer()) continue
        if (file.name != this) continue
        return true
    }

    return false
}

val existingWorlds: Set<String>
    get() {
        val result = mutableSetOf<String>()
        val possibleWorldContainers = Bukkit.getWorldContainer().listFiles() ?: return emptySet()

        for (file in possibleWorldContainers) {
            if (!file.isWorldContainer()) continue
            if (result.contains(file.name)) continue
            result.add(file.name)
        }

        return result
    }


fun String.loadAsWorld(): World? {
    if (this.isWorldContainer()) return null
    return Bukkit.getWorld(this) ?: Bukkit.createWorld(WorldCreator(this))
}