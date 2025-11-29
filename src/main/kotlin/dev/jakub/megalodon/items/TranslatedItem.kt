package dev.jakub.megalodon.dev.jakub.megalodon.items

import dev.jakub.megalodon.dev.jakub.megalodon.items.builder.SmartItem
import dev.jakub.megalodon.dev.jakub.megalodon.items.builder.applyMeta
import dev.jakub.megalodon.dev.jakub.megalodon.items.data.InteractionType
import dev.jakub.megalodon.dev.jakub.megalodon.text.translation.getValue
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

inline fun <reified T : ItemMeta> Player.smartTransItem(
    key: String,
    fallback: String = "",
    material: Material,
    descriptionKey: String = "",
    tagResolver: List<TagResolver> = emptyList(),
    interactionType: InteractionType = InteractionType.CLICK,
    crossinline builder: T.() -> Unit = {}
): SmartItem = smartTransItem<T>(key, fallback, this.locale(), material, descriptionKey, tagResolver, interactionType, builder)

inline fun <reified T : ItemMeta> smartTransItem(
    key: String,
    fallback: String = "",
    locale: Locale,
    material: Material,
    descriptionKey: String = "",
    tagResolver: List<TagResolver> = emptyList(),
    interactionType: InteractionType = InteractionType.CLICK,
    crossinline builder: T.() -> Unit = {}
): SmartItem {
    val itemStack = ItemStack(material)
    val name = locale.getValue(key, fallback)
    val description = locale.getValue(descriptionKey, "")
    itemStack.applyMeta<T>(name, description, tagResolver, interactionType, builder)
    return SmartItem(itemStack, interactionType)
}

inline fun basicSmartTransItem(
    key: String,
    fallback: String = "",
    locale: Locale,
    material: Material,
    descriptionKey: String = "",
    tagResolver: List<TagResolver> = emptyList(),
    interactionType: InteractionType = InteractionType.CLICK,
    crossinline builder: ItemMeta.() -> Unit = {}
): SmartItem = smartTransItem<ItemMeta>(key, fallback, locale, material, descriptionKey, tagResolver, interactionType, builder)

inline fun Player.basicSmartTransItem(
    key: String,
    fallback: String = "",
    material: Material,
    descriptionKey: String = "",
    tagResolver: List<TagResolver> = emptyList(),
    interactionType: InteractionType = InteractionType.CLICK,
    crossinline builder: ItemMeta.() -> Unit = {}
): SmartItem = this.smartTransItem<ItemMeta>(key, fallback, material, descriptionKey, tagResolver, interactionType, builder)