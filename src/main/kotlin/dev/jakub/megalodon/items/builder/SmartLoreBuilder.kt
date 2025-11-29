package dev.jakub.megalodon.dev.jakub.megalodon.items.builder

import com.google.common.collect.Lists
import dev.jakub.megalodon.dev.jakub.megalodon.annotations.InternalUse
import dev.jakub.megalodon.dev.jakub.megalodon.text.communication.CommunicationType
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.ComponentBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.adventureText
import net.kyori.adventure.text.Component
import org.bukkit.Input

class SmartLoreBuilder(
    private val description: MutableList<Component> = mutableListOf()
) {

    fun removeLast() = description.removeLast()

    fun insertFirst(component: Component) = description.addFirst(component)

    fun insertLast(component: Component) = description.addLast(component)

    fun addLore(Component: Component) = description.add(Component)

    fun addLore(
        text: String,
        builder: ComponentBuilder.() -> Unit
    ) = description.add(ComponentBuilder(text).apply(builder).build().component)

    fun removeLore(index: Int) = description.removeAt(index)

    fun build(): List<Component> = description
}

fun smartestLoreBuilder(input: String): List<Component> {
    if (input.isEmpty()) return emptyList()

    val strings = input.split(" ")
    if (strings.size <= 5) return listOf(input.stringToStyledLore())
    val subList = Lists.partition(strings, 4)
    val result = mutableListOf<Component>()

    subList.forEach { list ->
        val alreadyAString = buildString { list.forEach { append("$it ") } }
        result.add(alreadyAString.stringToStyledLore())
    }

    return result
}

@InternalUse
private fun String.stringToStyledLore(): Component {
    return adventureText("| $this") {
        type = CommunicationType.NONE
    }
}