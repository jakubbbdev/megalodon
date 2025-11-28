package dev.jakub.megalodon.dev.jakub.megalodon.text.title

import dev.jakub.megalodon.dev.jakub.megalodon.annotations.InternalUse
import dev.jakub.megalodon.dev.jakub.megalodon.text.communication.CommunicationType
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.adventureText
import dev.jakub.megalodon.dev.jakub.megalodon.text.title.configuration.TitleType
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.Title.Times
import java.time.Duration


inline fun adventureTitle(
    title: String,
    subtitle: String = "",
    builder: TitleBuilder.() -> Unit
) = TitleBuilder(title, subtitle).apply(builder).builder()

@InternalUse
class TitleBuilder(
    private val title: String,
    private val subtitle: String
) {
    private val currentStyles = mutableMapOf<TitleType, MutableSet<TextDecoration>>()

    fun italic(titleType: TitleType, enabled: Boolean = true) = apply {
        if (enabled) addStyle(titleType, TextDecoration.ITALIC)
    }

    fun bold(titleType: TitleType, enabled: Boolean = true) = apply {
        if (enabled) addStyle(titleType, TextDecoration.BOLD)
    }

    fun strikethrough(titleType: TitleType, enabled: Boolean = true) = apply {
        if (enabled) addStyle(titleType, TextDecoration.STRIKETHROUGH)
    }

    fun underlined(titleType: TitleType, enabled: Boolean = true) = apply {
        if (enabled) addStyle(titleType, TextDecoration.UNDERLINED)
    }

    fun obfuscated(titleType: TitleType, enabled: Boolean = true) = apply {
        if (enabled) addStyle(titleType, TextDecoration.OBFUSCATED)
    }

    private fun addStyle(titleType: TitleType, decoration: TextDecoration) {
        when (titleType) {
            TitleType.UP -> currentStyles.computeIfAbsent(TitleType.UP) { mutableSetOf() }.add(decoration)
            TitleType.DOWN -> currentStyles.computeIfAbsent(TitleType.DOWN) { mutableSetOf() }.add(decoration)
            TitleType.BOTH -> {
                currentStyles.computeIfAbsent(TitleType.UP) { mutableSetOf() }.add(decoration)
                currentStyles.computeIfAbsent(TitleType.DOWN) { mutableSetOf() }.add(decoration)
            }
        }
    }

    var resolver: Array<TagResolver>? = null
    var type: CommunicationType? = null

    var duration: Long = 3

    fun build(): AdventureTitle {
        var titleComponent = adventureText(title) {
            resolver = this@TitleBuilder.resolver
            type = this@TitleBuilder.type
            if (this@TitleBuilder.currentStyles[TitleType.UP].isNullOrEmpty()) return@adventureText
            currentStyles = this@TitleBuilder.currentStyles[TitleType.UP]!!
        }

        var subtitleComponent = adventureText(subtitle) {
            resolver = this@TitleBuilder.resolver
            type = CommunicationType.NONE
            if (this@TitleBuilder.currentStyles[TitleType.DOWN].isNullOrEmpty()) return@adventureText
            currentStyles = this@TitleBuilder.currentStyles[TitleType.DOWN]!!
        }

        val time = Times.times(Duration.ZERO, Duration.ofSeconds(duration), Duration.ZERO)
        val title = Title.title(titleComponent, subtitleComponent, time)
        return AdventureTitle(title, type?.sound)
    }
}