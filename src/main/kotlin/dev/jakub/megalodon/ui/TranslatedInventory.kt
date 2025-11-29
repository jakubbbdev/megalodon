package dev.jakub.megalodon.dev.jakub.megalodon.ui

import dev.jakub.megalodon.dev.jakub.megalodon.text.components.adventureText
import dev.jakub.megalodon.dev.jakub.megalodon.text.translation.getValue
import dev.jakub.megalodon.dev.jakub.megalodon.ui.builder.DisplayInventoryBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.ui.builder.InventoryBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.ui.builder.SmartClick
import dev.jakub.megalodon.dev.jakub.megalodon.ui.data.InventoryRows
import dev.jakub.megalodon.dev.jakub.megalodon.ui.data.InventorySlots
import dev.jakub.megalodon.dev.jakub.megalodon.ux.color.Colorization
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

inline fun Player.createTransInventory(
    key: String,
    fallback: String = "",
    rows: InventoryRows = InventoryRows.ROW6,
    crossinline builder: InventoryBuilder.() -> Unit = {}
) = InventoryBuilder(this.translateTitle(key, fallback), this, rows).apply(builder)

inline fun Player.openTransInventory(
    key: String,
    fallback: String = "",
    rows: InventoryRows = InventoryRows.ROW6,
    crossinline builder: InventoryBuilder.() -> Unit = {}
) = createInventory(this.translateTitle(key, fallback), rows, builder).open()

inline fun Player.createTransPageInventory(
    key: String,
    fallback: String = "", rows: InventoryRows = InventoryRows.ROW6,
    list: List<SmartClick> = emptyList(),
    from: InventorySlots = InventorySlots.SLOT1ROW1,
    to: InventorySlots = InventorySlots.SLOT9ROW6,
    crossinline builder: DisplayInventoryBuilder.() -> Unit = {}
) = DisplayInventoryBuilder(this.translateTitle(key, fallback), this, rows, list, from, to).apply(builder)

inline fun Player.openTransPageInventory(
    key: String,
    fallback: String = "",
    rows: InventoryRows = InventoryRows.ROW6,
    list: List<SmartClick> = emptyList(),
    from: InventorySlots = InventorySlots.SLOT1ROW1,
    to: InventorySlots = InventorySlots.SLOT9ROW6,
    crossinline builder: DisplayInventoryBuilder.() -> Unit = {}
) = DisplayInventoryBuilder(this.translateTitle(key, fallback), this, rows, list, from, to).apply(builder).open()

fun Player.translateTitle(key: String, fallback: String): Component {
    val text = this.locale().getValue(key, fallback)
    return adventureText(text) {
        color = Colorization.GRAY
        underlined(true)
    }
}