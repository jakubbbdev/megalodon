package dev.jakub.megalodon.dev.jakub.megalodon.schedule.time

enum class TaskTimeTypes {
    SECONDS,
    MINUTES,
    HOURS,
    DAYS
}

fun TaskTimeTypes.toMilliseconds(time: Long): Long {
    return when (this) {
        TaskTimeTypes.SECONDS -> time * 1000L
        TaskTimeTypes.MINUTES -> time * 1000 * 60
        TaskTimeTypes.HOURS -> time * 1000 * 60 * 60
        TaskTimeTypes.DAYS -> time * 1000 * 60 * 60 * 24
    }
}