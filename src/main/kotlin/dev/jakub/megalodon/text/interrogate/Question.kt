package dev.jakub.megalodon.dev.jakub.megalodon.text.interrogate

import dev.jakub.megalodon.dev.jakub.megalodon.event.unregister
import dev.jakub.megalodon.dev.jakub.megalodon.event.listen
import dev.jakub.megalodon.dev.jakub.megalodon.schedule.doLater
import dev.jakub.megalodon.dev.jakub.megalodon.schedule.doNow
import dev.jakub.megalodon.dev.jakub.megalodon.schedule.time.TaskTimeTypes
import dev.jakub.megalodon.dev.jakub.megalodon.text.communication.CommunicationType
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.AdventureMessage
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.adventureMessage
import dev.jakub.megalodon.dev.jakub.megalodon.text.sendText
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.Listener

inline fun Player.interrogate(
    questionMessage: AdventureMessage = adventureMessage("Please type your answer in the chat!") {
        type = CommunicationType.INFO
    },
    timeoutMessage: AdventureMessage = adventureMessage("You didn't answer!") {
        type = CommunicationType.ALERT
    },
    timeout: Long = 60,
    timeTypes: TaskTimeTypes = TaskTimeTypes.SECONDS,
    crossinline onAnswerReceived: (player: Player, answered: Boolean, chatEvent: AsyncChatEvent?) -> Unit
) {

    var message: Component? = null
    this.sendText(questionMessage)
    lateinit var event: Listener

    event = this.listen<AsyncChatEvent> { asyncChatEvent ->
        event.unregister()
        message = asyncChatEvent.message()
        doNow {
            onAnswerReceived.invoke(this@interrogate, true, asyncChatEvent)
        }
    }

    doLater(timeout, timeTypes) {
        event.unregister()
        if (message != null) return@doLater
        this.sendText(timeoutMessage)
        doNow {
            onAnswerReceived.invoke(this@interrogate, false, null)
        }
    }
}