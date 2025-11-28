package dev.jakub.megalodon.dev.jakub.megalodon.schedule

import dev.jakub.megalodon.dev.jakub.megalodon.annotations.InternalUse
import dev.jakub.megalodon.dev.jakub.megalodon.schedule.time.TaskTimeTypes
import dev.jakub.megalodon.dev.jakub.megalodon.schedule.time.toMilliseconds
import java.util.Timer
import java.util.TimerTask
import java.util.UUID

private val repeatingTasks: HashMap<UUID, Timer> = hashMapOf()

inline fun doAgain(
    delay: Long = 0,
    period: Long,
    taskTimeTypes: TaskTimeTypes = TaskTimeTypes.SECONDS,
    noinline function: RepeatTaskBuilder.(Long) -> Unit = {},
) = RepeatTaskBuilder(
    false,
    taskTimeTypes.toMilliseconds(delay),
    taskTimeTypes.toMilliseconds(period),
    function
).start()

inline fun doAgainAsync(
    delay: Long = 0,
    period: Long,
    taskTimeTypes: TaskTimeTypes = TaskTimeTypes.SECONDS,
    noinline function: RepeatTaskBuilder.(Long) -> Unit = {},
) = RepeatTaskBuilder(true, taskTimeTypes.toMilliseconds(delay), taskTimeTypes.toMilliseconds(period), function).start()

fun stopRepeatingTask(uuid: UUID) {
    repeatingTasks[uuid]?.cancel()
}

@InternalUse
data class RepeatTaskBuilder(
    private val async: Boolean,
    private val delay: Long,
    private val period: Long,
    private val function: RepeatTaskBuilder.(Long) -> Unit = {},
) {
    private val timer = Timer()

    private var iteration: Long = 0
    private var stopped = false

    fun start(): UUID {
        val uuid = UUID.randomUUID()
        repeatingTasks.putIfAbsent(uuid, timer)

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (stopped) return
                if (async) doNowAsync {
                    function(iteration)
                } else doNow {
                    function(iteration)
                }
                iteration++
            }
        }, delay, period)
        return uuid
    }

    fun stop() {
        timer.cancel()
    }
}