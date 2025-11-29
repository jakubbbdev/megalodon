package dev.jakub.megalodon.dev.jakub.megalodon.bossbar.builder

import dev.jakub.megalodon.dev.jakub.megalodon.annotations.InternalUse
import dev.jakub.megalodon.dev.jakub.megalodon.bossbar.manager.BossBarManager
import dev.jakub.megalodon.dev.jakub.megalodon.bossbar.manager.getBossBarManager
import dev.jakub.megalodon.dev.jakub.megalodon.schedule.doLater
import dev.jakub.megalodon.dev.jakub.megalodon.schedule.time.TaskTimeTypes
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.ComponentBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.text.components.adventureText
import dev.jakub.megalodon.dev.jakub.megalodon.ux.color.Colorization
import dev.jakub.megalodon.dev.jakub.megalodon.ux.color.asBossBarColor
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

@InternalUse
fun createBossBarBuilder(
    player: Player,
    text: Component,
    progress: Float
): BossBarBuilder = BossBarBuilder(player, text, progress)

@InternalUse
fun createBossBarBuilder(
    player: Player,
    text: String,
    progress: Float
): BossBarBuilder = BossBarBuilder(player, adventureText(text), progress)

@InternalUse
class BossBarBuilder(
    private val player: Player,
    private var text: Component,
    private var progress: Float
) {
    private var color: BossBar.Color = Colorization.WHITE.asBossBarColor()
    private var overlay: BossBar.Overlay = BossBar.Overlay.PROGRESS
    private var key: String? = null
    private var hideAfterDuration: Long? = null
    private var hideAfterTimeType: TaskTimeTypes = TaskTimeTypes.SECONDS
    private var onHide: (() -> Unit)? = null

    fun color(colorization: Colorization) {
        this.color = colorization.asBossBarColor()
    }

    fun color(color: BossBar.Color) {
        this.color = color
    }

    fun overlay(overlay: BossBar.Overlay) {
        this.overlay = overlay
    }

    fun key(key: String) {
        this.key = key
    }

    fun hideAfter(duration: Long, timeType: TaskTimeTypes = TaskTimeTypes.SECONDS) {
        this.hideAfterDuration = duration
        this.hideAfterTimeType = timeType
    }

    fun onHide(callback: () -> Unit) {
        this.onHide = callback
    }

    fun text(text: Component) {
        this.text = text
    }

    fun text(text: String, builder: ComponentBuilder.() -> Unit = {}) {
        this.text = adventureText(text, builder)
    }

    fun progress(progress: Float) {
        this.progress = progress.coerceIn(0f, 1f)
    }

    fun show() {
        val bossBar = BossBar.bossBar(text, progress, color, overlay)
        getBossBarManager().showBossBar(player, bossBar, key)

        hideAfterDuration?.let { duration ->
            doLater(duration, hideAfterTimeType) {
                hideBossBar()
                onHide?.invoke()
            }
        }
    }

    private fun hideBossBar() {
        getBossBarManager().hideBossBar(player, key)
    }
}

