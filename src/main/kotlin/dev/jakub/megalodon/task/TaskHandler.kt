package dev.jakub.megalodon.dev.jakub.megalodon.task

import java.util.UUID

private val taskRegistry: MutableMap<UUID, Task> = mutableMapOf()

fun Task.register() {
    taskRegistry[this.uuid] = this
}

fun unregisterTask(uuid: UUID) {
    taskRegistry[uuid]?.stop()
    taskRegistry.remove(uuid)
}

fun Task.unregister() {
    taskRegistry[uuid]?.stop()
    taskRegistry.remove(uuid)
}

fun runTask(uuid: UUID) {
    taskRegistry[uuid]?.run()
}