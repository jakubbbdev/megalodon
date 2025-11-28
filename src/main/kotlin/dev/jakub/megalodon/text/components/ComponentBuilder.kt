package dev.jakub.megalodon.dev.jakub.megalodon.text.components

import dev.jakub.megalodon.dev.jakub.megalodon.annotations.InternalUse
import dev.jakub.megalodon.dev.jakub.megalodon.text.communication.CommunicationType
import dev.jakub.megalodon.dev.jakub.megalodon.ux.color.Colorization
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver


inline fun adventureText(
    text: String,
    builder: ComponentBuilder.() -> Unit = {}
) = adventureMessage(text, builder).component

inline fun adventureMessage(
    text: String,
    builder: ComponentBuilder.() -> Unit = {}
) = ComponentBuilder(text).apply(builder).build()
@InternalUse
class ComponentBuilder(private val input: String) {

    var currentStyles = mutableSetOf<TextDecoration>()
    private var removeStyles = mutableSetOf<TextDecoration>()

    var clickEvent: ClickEvent? = null
    var hoverEvent: HoverEvent<*>? = null

    var type: CommunicationType? = null
    var color: Colorization? = null
    var resolver: Array<out TagResolver>? = null
    var renderMode: RenderMode = RenderMode.DEFAULT

    fun italic(enabled: Boolean) = toggleDecoration(TextDecoration.ITALIC, enabled)
    fun bold(enabled: Boolean) = toggleDecoration(TextDecoration.BOLD, enabled)
    fun strikethrough(enabled: Boolean) = toggleDecoration(TextDecoration.STRIKETHROUGH, enabled)
    fun underlined(enabled: Boolean) = toggleDecoration(TextDecoration.UNDERLINED, enabled)
    fun obfuscated(enabled: Boolean) = toggleDecoration(TextDecoration.OBFUSCATED, enabled)

    private fun toggleDecoration(decoration: TextDecoration, enabled: Boolean) {
        if (enabled) currentStyles.add(decoration) else removeStyles.add(decoration)
    }

    fun onOpenURL(url: String) = applyClickEvent(ClickEvent.openUrl(url))
    fun onOpenFile(file: String) = applyClickEvent(ClickEvent.openFile(file))
    fun onRunCommand(command: String) = applyClickEvent(ClickEvent.runCommand(command))
    fun onSuggestCommand(command: String) = applyClickEvent(ClickEvent.suggestCommand(command))
    fun onCopyClipboard(text: String) = applyClickEvent(ClickEvent.copyToClipboard(text))
    private fun applyClickEvent(event: ClickEvent) { this.clickEvent = event }
    fun onHoverText(text: Component) { this.hoverEvent = HoverEvent.showText(text) }

    fun build(): AdventureMessage {
        var textComponent =
            if (resolver == null) MiniMessage.miniMessage().deserialize(input)
            else MiniMessage.miniMessage().deserialize(input, *resolver!!)

        var styleBuilder = textComponent.style()
        currentStyles.forEach { styleBuilder = styleBuilder.decoration(it, true) }
        removeStyles.forEach { styleBuilder = styleBuilder.decoration(it, false) }

        clickEvent?.let { styleBuilder = styleBuilder.clickEvent(it) }
        hoverEvent?.let { styleBuilder = styleBuilder.hoverEvent(hoverEvent) }
        color?.let { styleBuilder= styleBuilder.color(it.rgbLike) }

        if (renderMode == RenderMode.LORE) {
            styleBuilder = styleBuilder
                .decoration(TextDecoration.ITALIC, false)
            if (color == null && type != null) {
                styleBuilder = styleBuilder.color(type!!.color.rgbLike)
            }
        }

        type?.let {
            styleBuilder = styleBuilder.color(it.color.rgbLike)
            textComponent = Component.text(it.icon).append(textComponent)
        }

        return AdventureMessage(textComponent.style(styleBuilder).asComponent(), type?.sound)
    }
}

enum class RenderMode {
    DEFAULT,
    LORE
}