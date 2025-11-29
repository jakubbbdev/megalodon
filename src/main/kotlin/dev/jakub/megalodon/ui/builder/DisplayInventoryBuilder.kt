package dev.jakub.megalodon.dev.jakub.megalodon.ui.builder

import com.google.common.collect.Lists
import dev.jakub.megalodon.dev.jakub.megalodon.items.builder.SmartItem
import dev.jakub.megalodon.dev.jakub.megalodon.items.data.InteractionType
import dev.jakub.megalodon.dev.jakub.megalodon.ui.data.InventoryRows
import dev.jakub.megalodon.dev.jakub.megalodon.ui.data.InventorySlots
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

class DisplayInventoryBuilder(
    title: Component,
    holder: Player,
    rows: InventoryRows = InventoryRows.ROW6,
    private val list: List<SmartClick>,
    private val from: InventorySlots,
    private val to: InventorySlots
) : InventoryBuilder(title, holder, rows) {

    private val maxItems: Int = (to.value + 1) - from.value
    private val pages: List<List<SmartClick>> = if (list.isEmpty()) listOf() else Lists.partition(list, maxItems)
    private val maxPage: Int = if (pages.isEmpty()) 1 else pages.count()
    private var currentPageIndex = 0

    // Navigation button data
    private var leftSlot: InventorySlots? = null
    private var rightSlot: InventorySlots? = null
    private var leftActiveItem: SmartItem? = null
    private var rightActiveItem: SmartItem? = null
    private var leftInactiveItem: SmartItem? = null
    private var rightInactiveItem: SmartItem? = null

    init {
        applyPage()
    }

    private fun applyPage() {
        for (slotIndex in from.value..to.value) {
            removeSlot(InventorySlots.values().first { it.value == slotIndex })
        }

        if (pages.isEmpty()) return

        val currentPage = pages[currentPageIndex]

        for ((i, slotIndex) in (from.value..to.value).withIndex()) {
            if (i >= currentPage.size) break
            val currentItem = currentPage[i]
            setSlot(slotIndex, currentItem.item, currentItem.action)
        }

        leftSlot?.let { slot ->
            if (currentPageIndex > 0) {
                leftActiveItem?.let {
                    setBlockedSlot(slot, it, Consumer { _ ->
                        currentPageIndex--
                        applyPage()
                    })
                }
            } else {
                leftInactiveItem?.let { setBlockedSlot(slot, it) }
            }
        }

        rightSlot?.let { slot ->
            if (currentPageIndex < maxPage - 1) {
                rightActiveItem?.let {
                    setBlockedSlot(slot, it, Consumer { _ ->
                        currentPageIndex++
                        applyPage()
                    })
                }
            } else {
                rightInactiveItem?.let { setBlockedSlot(slot, it) }
            }
        }
    }

    fun pageLeft(slot: InventorySlots, activeDisplay: ItemStack, inactiveDisplay: ItemStack) {
        leftSlot = slot
        leftActiveItem = SmartItem(activeDisplay, InteractionType.PAGE_TURN)
        leftInactiveItem = SmartItem(inactiveDisplay, InteractionType.ERROR)
        applyPage()
    }

    fun pageRight(slot: InventorySlots, activeDisplay: ItemStack, inactiveDisplay: ItemStack) {
        rightSlot = slot
        rightActiveItem = SmartItem(activeDisplay, InteractionType.PAGE_TURN)
        rightInactiveItem = SmartItem(inactiveDisplay, InteractionType.ERROR)
        applyPage()
    }

    fun emptyPage(slot: InventorySlots, display: ItemStack) {
        if (list.isEmpty()) {
            setBlockedSlot(slot, SmartItem(display, InteractionType.ERROR))
        }
    }
}