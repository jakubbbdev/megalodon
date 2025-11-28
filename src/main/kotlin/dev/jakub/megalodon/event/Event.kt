package dev.jakub.megalodon.dev.jakub.megalodon.event

import org.bukkit.entity.Player
import org.bukkit.event.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

fun Listener.unregister() = HandlerList.unregisterAll(this)

inline fun <reified T : Event> listen(
    priority: EventPriority = EventPriority.NORMAL,
    crossinline onEvent: (event: T) -> Unit,
): Listener {

    val wrapper = object : WrapperListener<T>(priority) {
        override fun onEvent(event: T) = onEvent.invoke(event)
    }

    wrapper.subscribe()
    return wrapper
}

inline fun <reified T : PlayerEvent> Player.listen(
    priority: EventPriority = EventPriority.NORMAL,
    crossinline onEvent: (event: T) -> Unit,
): Listener {

    val wrapper = object : WrapperListener<T>(priority) {
        override fun onEvent(event: T) = onEvent.invoke(event)
    }

    wrapper.subscribeForPlayer(this)
    return wrapper
}

inline fun <reified T : Event> listenCancelled(eventPriority: EventPriority = EventPriority.LOWEST): Listener =
    listen<T>(eventPriority) { event ->
        if (event is Cancellable) event.isCancelled = true
    }

inline fun ItemStack.listenClick(
    priority: EventPriority = EventPriority.NORMAL,
    crossinline onEvent: (event: InventoryClickEvent) -> Unit,
) = listen<InventoryClickEvent>(priority) { event ->
    if (event.clickedInventory == this) onEvent.invoke(event)
}

inline fun ItemStack.listenInteract(
    priority: EventPriority = EventPriority.NORMAL,
    crossinline onEvent: (event: PlayerInteractEvent) -> Unit
) = listen<PlayerInteractEvent>(priority) { event ->
    if (event.item == this) onEvent.invoke(event)
}