package dev.jakub.megalodon.dev.jakub.megalodon.text.components

import net.kyori.adventure.text.Component
import org.bukkit.Sound

data class AdventureMessage(
    val component: Component,
    val sound: Sound?
)