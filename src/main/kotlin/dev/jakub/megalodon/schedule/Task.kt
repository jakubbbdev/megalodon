package dev.jakub.megalodon.dev.jakub.megalodon.schedule

import dev.jakub.megalodon.dev.jakub.megalodon.MegalodonInstance
import dev.jakub.megalodon.dev.jakub.megalodon.annotations.InternalUse
import org.bukkit.Bukkit

inline fun doNow(
    noinline function: () -> Unit = {}
) = doTask(false, function)

inline fun doNowAsync(
    noinline function: () -> Unit = {}
) = doTask(true, function)

@InternalUse
inline fun doTask(
    async: Boolean = false,
    noinline function: () -> Unit = {}
) {
    if (async) {
        Bukkit.getScheduler().runTaskAsynchronously(MegalodonInstance, function)
    } else {
        Bukkit.getScheduler().runTask(MegalodonInstance, function)
    }
}