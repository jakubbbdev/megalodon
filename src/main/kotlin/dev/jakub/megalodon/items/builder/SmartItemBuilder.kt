package dev.jakub.megalodon.dev.jakub.megalodon.items.builder

import dev.jakub.megalodon.dev.jakub.megalodon.annotations.InternalUse
import dev.jakub.megalodon.dev.jakub.megalodon.items.data.InteractionType
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.adventureText
import jdk.jfr.Description
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

data class SmartItem(
    val itemStack: ItemStack,
    val interactionType: InteractionType
)

@InternalUse
inline fun <reified T: ItemMeta> ItemStack.applyMeta(
    name: String,
    description: String,
    tagResolver: List<TagResolver>,
    interactionType: InteractionType,
    builder: T.() -> Unit = {}
) {
    val meta = this.itemMeta as T
    meta.apply { builder(this) }
    val title = adventureText(name) {
        if (interactionType == InteractionType.DISPLAY_CLICK) {
            underlined(true)
        }
        italic(false)
        color = interactionType.color
        resolver = tagResolver.toTypedArray()
    }

    meta.setDisplayName(LegacyComponentSerializer.legacySection().serialize(title))
    val currentDescription = smartestLoreBuilder(description)
    if (currentDescription.isNotEmpty()) {
        val stringLore = currentDescription.map { LegacyComponentSerializer.legacySection().serialize(it) }
        meta.lore = stringLore
    }
    this.itemMeta = meta
}