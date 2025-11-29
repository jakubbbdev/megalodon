package dev.jakub.megalodon.dev.jakub.megalodon.items

import dev.jakub.megalodon.dev.jakub.megalodon.items.builder.SmartItem
import dev.jakub.megalodon.dev.jakub.megalodon.items.builder.applyMeta
import dev.jakub.megalodon.dev.jakub.megalodon.items.data.InteractionType
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CrossbowMeta
import org.bukkit.inventory.meta.ItemMeta
import org.eclipse.sisu.Description

inline fun <reified T : ItemMeta> createSmartItem(
    name: String,
    material: Material,
    description: String = "",
    tagResolver: List<TagResolver> = emptyList(),
    interactionType: InteractionType = InteractionType.CLICK,
    crossinline builder: T.() -> Unit = {}
): SmartItem {
    val itemStack = ItemStack(material)
    itemStack.applyMeta<T>(name, description, tagResolver, interactionType, builder)
    return SmartItem(itemStack, interactionType)
}

inline fun smartItemWithoutMeta(
    name: String,
    material: Material,
    description: String = "",
    tagResolver: List<TagResolver> = emptyList(),
    interactionType: InteractionType = InteractionType.CLICK,
    crossinline builder: ItemMeta.() -> Unit = {}
) : SmartItem = createSmartItem<ItemMeta>(name, material, description, tagResolver, interactionType, builder)

inline fun <reified T: ItemMeta> basicItem(
    material: Material,
    crossinline builder: T.() -> Unit = {}
) : ItemStack {
    val itemStack = ItemStack(material)
    val meta = itemStack.itemMeta as T
    meta.apply(builder)
    itemStack.itemMeta = meta
    return itemStack
}

inline fun basicItemWithoutMeta(
    material: Material,
    crossinline builder: ItemMeta.() -> Unit = {}
): ItemStack = basicItem<ItemMeta>(material, builder)