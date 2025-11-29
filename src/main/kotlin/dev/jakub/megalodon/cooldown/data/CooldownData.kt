package dev.jakub.megalodon.dev.jakub.megalodon.cooldown.data

import dev.jakub.megalodon.dev.jakub.megalodon.schedule.time.TaskTimeTypes
import java.util.UUID

data class CooldownData(
    val playerUuid: UUID,
    val key: String,
    val endTime: Long,
    val duration: Long,
    val timeType: TaskTimeTypes
) {
    val remainingTime: Long
        get() = (endTime - System.currentTimeMillis()).coerceAtLeast(0)

    val isActive: Boolean
        get() = System.currentTimeMillis() < endTime
}

