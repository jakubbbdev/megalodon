package dev.jakub.megalodon.dev.jakub.megalodon.schedule

import dev.jakub.megalodon.dev.jakub.megalodon.schedule.time.TaskTimeTypes
import dev.jakub.megalodon.dev.jakub.megalodon.schedule.time.toMilliseconds
import java.util.Timer
import kotlin.concurrent.schedule


inline fun doLater(
    delay: Long,
    taskTimeTypes: TaskTimeTypes = TaskTimeTypes.SECONDS,
    crossinline function: () -> Unit = {}
) = Timer(false).schedule(taskTimeTypes.toMilliseconds(delay)) {
    doNow {
        function.invoke()
    }
}

inline fun doLaterAsync(
    delay: Long,
    taskTimeTypes: TaskTimeTypes = TaskTimeTypes.SECONDS,
    crossinline function: () -> Unit = {}
) = Timer(false).schedule(taskTimeTypes.toMilliseconds(delay)) {
    doNowAsync {
        function.invoke()
    }
}