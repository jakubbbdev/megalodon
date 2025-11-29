package dev.jakub.megalodon.dev.jakub.megalodon.ui

import dev.jakub.megalodon.dev.jakub.megalodon.ui.builder.DisplayInventoryBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.ui.builder.InventoryBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.ui.builder.SmartClick
import dev.jakub.megalodon.dev.jakub.megalodon.ui.data.InventoryRows
import dev.jakub.megalodon.dev.jakub.megalodon.ui.data.InventorySlots
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

inline fun Player.createInventory(
    title: Component,
    rows: InventoryRows = InventoryRows.ROW6,
    crossinline builder: InventoryBuilder.() -> Unit = {}
) = InventoryBuilder(title, this, rows).apply(builder)

inline fun Player.openInventory(
    title: Component,
    rows: InventoryRows = InventoryRows.ROW6,
    crossinline builder: InventoryBuilder.() -> Unit = {}
) = createInventory(title, rows, builder).open()

inline fun Player.createPageInventory(
    title: Component,
    rows: InventoryRows = InventoryRows.ROW6,
    list: List<SmartClick> = emptyList(),
    from: InventorySlots = InventorySlots.SLOT1ROW1,
    to: InventorySlots = InventorySlots.SLOT9ROW6,
    crossinline builder: DisplayInventoryBuilder.() -> Unit = {}
) = DisplayInventoryBuilder(title, this, rows, list, from, to).apply(builder)

inline fun Player.openPageInventory(
    title: Component,
    rows: InventoryRows = InventoryRows.ROW6,
    list: List<SmartClick> = emptyList(),
    from: InventorySlots = InventorySlots.SLOT1ROW1,
    to: InventorySlots = InventorySlots.SLOT9ROW6,
    crossinline builder: DisplayInventoryBuilder.() -> Unit = {}
) = DisplayInventoryBuilder(title, this, rows, list, from, to).apply(builder).open()