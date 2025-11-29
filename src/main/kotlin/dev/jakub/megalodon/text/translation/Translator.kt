package dev.jakub.megalodon.dev.jakub.megalodon.text.translation

import dev.jakub.megalodon.dev.jakub.megalodon.schedule.time.TaskTimeTypes
import dev.jakub.megalodon.dev.jakub.megalodon.text.communication.CommunicationType
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.ComponentBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.adventureMessage
import dev.jakub.megalodon.dev.jakub.megalodon.text.interrogate.interrogate
import dev.jakub.megalodon.dev.jakub.megalodon.text.sendText
import dev.jakub.megalodon.dev.jakub.megalodon.text.sendTitle
import dev.jakub.megalodon.dev.jakub.megalodon.text.title.TitleBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.text.title.adventureTitle
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.entity.Player
import java.util.*

private var fallbackLocale = Locale.ENGLISH

fun setFallbackLocale(locale: Locale) {
    fallbackLocale = locale
}

fun Player.sendTransText(key: String, fallback: String = "", builder: ComponentBuilder.() -> Unit = {}) {
    val message = this.locale().getValue(key, fallback)
    this.sendText(adventureMessage(message, builder))
}

fun Player.sendTransTitle(
    titleKey: String,
    titleFallback: String = "",
    subtitleKey: String,
    subtitleFallback: String = "",
    builder: TitleBuilder.() -> Unit = {}
) {
    val title = this.locale().getValue(titleKey, titleFallback)
    val subTitle = this.locale().getValue(subtitleKey, subtitleFallback)
    this.sendTitle(adventureTitle(title, subTitle, builder))
}

inline fun Player.interrogateTrans(
    questionKey: String,
    timeoutKey: String,
    timeout: Long = 60,
    timeTypes: TaskTimeTypes = TaskTimeTypes.SECONDS,
    crossinline onAnswerReceived: (player: Player, answered: Boolean, chatEvent: AsyncChatEvent?) -> Unit
) {

    val questionMessage = adventureMessage(this.locale().getValue(questionKey)) {
        type = CommunicationType.INFO
    }
    val timeoutMessage = adventureMessage(this.locale().getValue(timeoutKey)) {
        type = CommunicationType.ALERT
    }

    this.interrogate(questionMessage, timeoutMessage, timeout, timeTypes, onAnswerReceived)
}

fun Locale.getValue(key: String, fallback: String = ""): String {
    val bundle = getBundle()
    return if (bundle.containsKey(key)) bundle.getString(key) else fallback
}

private fun Locale.getBundle(): ResourceBundle {
    return try {
        ResourceBundle.getBundle("messages", this)
    } catch (exception: Exception) {
        ResourceBundle.getBundle("messages", fallbackLocale)
    }
}