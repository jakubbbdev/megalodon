package dev.jakub.megalodon.dev.jakub.megalodon.text

import dev.jakub.megalodon.dev.jakub.megalodon.text.components.AdventureMessage
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.ComponentBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.adventureMessage
import org.bukkit.entity.Player

fun Player.sendText(
    text: String = "",
    builder: ComponentBuilder.() -> Unit = {}
) = this.sendText(adventureMessage(text, builder))

fun Player.sendText(adventureMessage: AdventureMessage) {
    this.sendMessage(adventureMessage.component)
    adventureMessage.sound?.let { sound -> this.playSound(this, sound, 1f, 1f) }
}