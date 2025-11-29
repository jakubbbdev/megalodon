package dev.jakub.megalodon.dev.jakub.megalodon.ui.builder

import dev.jakub.megalodon.dev.jakub.megalodon.items.builder.SmartItem
import org.bukkit.event.inventory.InventoryClickEvent
import java.util.function.Consumer

data class SmartClick(
    val item: SmartItem,
    val action: Consumer<InventoryClickEvent>
)