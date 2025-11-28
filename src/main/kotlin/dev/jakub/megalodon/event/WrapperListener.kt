package dev.jakub.megalodon.dev.jakub.megalodon.event

import dev.jakub.megalodon.dev.jakub.megalodon.MegalodonInstance
import dev.jakub.megalodon.dev.jakub.megalodon.annotations.InternalUse
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent

@InternalUse
abstract class WrapperListener<T: Event> (
    val priority: EventPriority
) : Listener{
    abstract fun onEvent(event: T)
}

@InternalUse
inline fun <reified T: Event> WrapperListener<T>.subscribe() =
    Bukkit.getPluginManager().registerEvent(T::class.java, this, priority, { _, event ->
        if (event is T) {
            onEvent(event)
        }
    }, MegalodonInstance)

@InternalUse
inline fun <reified T: PlayerEvent> WrapperListener<T>.subscribeForPlayer(player: Player) =
    Bukkit.getPluginManager().registerEvent(T::class.java, this, priority, { _, event ->
        if (event is T && event.player.uniqueId == player.uniqueId) {
            onEvent(event)
        }
    }, MegalodonInstance)
