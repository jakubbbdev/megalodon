package dev.jakub.megalodon.dev.jakub.megalodon.cooldown

import dev.jakub.megalodon.dev.jakub.megalodon.cooldown.data.CooldownData
import dev.jakub.megalodon.dev.jakub.megalodon.cooldown.manager.CooldownManager
import dev.jakub.megalodon.dev.jakub.megalodon.cooldown.manager.getCooldownManager
import dev.jakub.megalodon.dev.jakub.megalodon.schedule.time.TaskTimeTypes
import org.bukkit.entity.Player

fun Player.isOnCooldown(key: String): Boolean {
    return getCooldownManager().isOnCooldown(this, key)
}

fun Player.getCooldownRemaining(key: String): Long {
    return getCooldownManager().getCooldownRemaining(this, key)
}

fun Player.setCooldown(
    key: String,
    duration: Long,
    timeType: TaskTimeTypes = TaskTimeTypes.SECONDS
) {
    getCooldownManager().setCooldown(this, key, duration, timeType)
}

fun Player.cooldown(
    key: String,
    duration: Long,
    timeType: TaskTimeTypes = TaskTimeTypes.SECONDS
) {
    setCooldown(key, duration, timeType)
}

fun Player.removeCooldown(key: String) {
    getCooldownManager().removeCooldown(this, key)
}

fun Player.clearCooldowns() {
    getCooldownManager().clearCooldowns(this)
}

fun Player.getCooldownData(key: String): CooldownData? {
    return getCooldownManager().getCooldownData(this, key)
}

fun Player.hasCooldown(key: String): Boolean = isOnCooldown(key)

fun Player.getCooldownRemainingFormatted(key: String): String {
    val remaining = getCooldownRemaining(key)
    if (remaining <= 0) return "0s"
    
    val seconds = remaining / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    
    return when {
        days > 0 -> "${days}d ${hours % 24}h"
        hours > 0 -> "${hours}h ${minutes % 60}m"
        minutes > 0 -> "${minutes}m ${seconds % 60}s"
        else -> "${seconds}s"
    }
}

