package dev.jakub.megalodon.dev.jakub.megalodon.cooldown.manager

import dev.jakub.megalodon.dev.jakub.megalodon.annotations.InternalUse
import dev.jakub.megalodon.dev.jakub.megalodon.cooldown.data.CooldownData
import dev.jakub.megalodon.dev.jakub.megalodon.schedule.doAgain
import dev.jakub.megalodon.dev.jakub.megalodon.schedule.time.TaskTimeTypes
import dev.jakub.megalodon.dev.jakub.megalodon.schedule.time.toMilliseconds
import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@InternalUse
private val cooldownRegistry: MutableMap<UUID, MutableMap<String, CooldownData>> = ConcurrentHashMap()

@InternalUse
fun getCooldownManager(): CooldownManager = CooldownManager

@InternalUse
object CooldownManager {
    fun isOnCooldown(player: Player, key: String): Boolean {
        val cooldown = getCooldownData(player, key) ?: return false
        return cooldown.isActive
    }

    fun getCooldownRemaining(player: Player, key: String): Long {
        val cooldown = getCooldownData(player, key) ?: return 0
        return cooldown.remainingTime
    }

    fun setCooldown(
        player: Player,
        key: String,
        duration: Long,
        timeType: TaskTimeTypes = TaskTimeTypes.SECONDS
    ) {
        val playerCooldowns = cooldownRegistry.computeIfAbsent(player.uniqueId) { ConcurrentHashMap() }
        val endTime = System.currentTimeMillis() + timeType.toMilliseconds(duration)
        
        playerCooldowns[key] = CooldownData(
            playerUuid = player.uniqueId,
            key = key,
            endTime = endTime,
            duration = duration,
            timeType = timeType
        )
    }

    fun removeCooldown(player: Player, key: String) {
        cooldownRegistry[player.uniqueId]?.remove(key)
    }

    fun clearCooldowns(player: Player) {
        cooldownRegistry.remove(player.uniqueId)
    }

    fun getCooldownData(player: Player, key: String): CooldownData? {
        val cooldown = cooldownRegistry[player.uniqueId]?.get(key) ?: return null
        if (!cooldown.isActive) {
            removeCooldown(player, key)
            return null
        }
        return cooldown
    }

    fun getAllCooldowns(player: Player): Map<String, CooldownData> {
        val playerCooldowns = cooldownRegistry[player.uniqueId] ?: return emptyMap()
        return playerCooldowns.filterValues { it.isActive }
    }

    fun cleanupExpiredCooldowns() {
        cooldownRegistry.values.forEach { playerCooldowns ->
            playerCooldowns.entries.removeIf { !it.value.isActive }
        }
        cooldownRegistry.entries.removeIf { it.value.isEmpty() }
    }

    fun startCleanupTask() {
        doAgain(period = 60, taskTimeTypes = TaskTimeTypes.SECONDS) {
            cleanupExpiredCooldowns()
        }
    }
}

