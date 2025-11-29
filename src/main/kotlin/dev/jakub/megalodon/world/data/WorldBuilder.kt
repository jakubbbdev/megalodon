package dev.jakub.megalodon.dev.jakub.megalodon.world.data

import dev.jakub.megalodon.dev.jakub.megalodon.event.listen
import dev.jakub.megalodon.dev.jakub.megalodon.event.unregister
import dev.jakub.megalodon.dev.jakub.megalodon.schedule.doLater
import dev.jakub.megalodon.dev.jakub.megalodon.world.generator.VoidGenerator
import org.bukkit.Difficulty
import org.bukkit.GameRule
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.event.world.WorldLoadEvent

data class WorldBuilder(
    val name: String,
    val generationTypes: WorldGenerationTypes,
    val environment: World.Environment,
    val isBuildingWorld: Boolean
) {

    fun build(): World? {
        val worldGenerator = WorldCreator(name)

        worldGenerator.environment(environment)
        worldGenerator.modifyGeneration(generationTypes)

        if (isBuildingWorld) {
            worldGenerator.generateStructures(false)
            worldGenerator.generator(VoidGenerator())
            applyGameRules(name)
        }

        return worldGenerator.createWorld()
    }
}

private fun applyGameRules(name: String) {
    val loadEvent = listen<WorldLoadEvent> { event ->
        if (event.world.name != name) return@listen
        val world = event.world

        world.difficulty = Difficulty.PEACEFUL

        world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0)
        world.setGameRule(GameRule.DISABLE_RAIDS, true)
        world.setGameRule(GameRule.DO_FIRE_TICK, false)
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
        world.setGameRule(GameRule.MOB_GRIEFING, false)
        world.setGameRule(GameRule.DO_WARDEN_SPAWNING, false)
        world.setGameRule(GameRule.DO_TRADER_SPAWNING, false)
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
        world.setGameRule(GameRule.DO_ENTITY_DROPS, false)
        world.setGameRule(GameRule.DO_ENTITY_DROPS, false)
        world.setGameRule(GameRule.KEEP_INVENTORY, true)
    }

    doLater(2000) {
        loadEvent.unregister()
    }
}