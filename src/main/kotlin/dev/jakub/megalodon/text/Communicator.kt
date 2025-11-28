package dev.jakub.megalodon.dev.jakub.megalodon.text

import dev.jakub.megalodon.dev.jakub.megalodon.schedule.doAgain
import dev.jakub.megalodon.dev.jakub.megalodon.schedule.doAgainAsync
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.AdventureMessage
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.ComponentBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.adventureMessage
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.adventureText
import dev.jakub.megalodon.dev.jakub.megalodon.text.title.AdventureTitle
import dev.jakub.megalodon.dev.jakub.megalodon.text.title.TitleBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.text.title.adventureTitle
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

fun Player.sendText(
    text: String = "",
    builder: ComponentBuilder.() -> Unit = {}
) = this.sendText(adventureMessage(text, builder))

fun Player.sendText(adventureMessage: AdventureMessage) {
    this.sendMessage(adventureMessage.component)
    adventureMessage.sound?.let { sound -> this.playSound(this, sound, 1f, 1f) }
}

fun Player.sendTitle(adventureTitle: AdventureTitle) {
    this.showTitle(adventureTitle.title)
    adventureTitle.sound?.let { sound -> this.playSound(this, sound, 1f, 1f) }
}

fun Player.sendTitle(
    title: String,
    subTitle: String = "",
    builder: TitleBuilder.() -> Unit = {}
) = this.sendTitle(adventureTitle(title, subTitle, builder))

fun Player.showActionbar(component: Component) {
    doAgainAsync(0, 3) {
        this@showActionbar.sendActionBar(component)
    }
}

inline fun Player.showActionbar(
    text: String,
    crossinline builder: ComponentBuilder.() -> Unit = {}
) = this.showActionbar(adventureText(text, builder))

fun Player.showActionBars(textList: List<Component>) {
    var textIndex = 0

    doAgain(0, 3) {
        if (textIndex >= textList.size) textIndex = 0
        this@showActionBars.sendActionBar(textList[textIndex])
        textIndex++
    }
}

inline fun Player.sendActionBars(
    textList: List<String>,
    crossinline builder: ComponentBuilder.() -> Unit = {}
) {
    val componentList = textList.map { ComponentBuilder(it).apply(builder).build().component }
    this.showActionBars(componentList)
}