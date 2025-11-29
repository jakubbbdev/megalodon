package dev.jakub.megalodon.dev.jakub.megalodon.bossbar

import dev.jakub.megalodon.dev.jakub.megalodon.bossbar.builder.BossBarBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.bossbar.builder.createBossBarBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.bossbar.manager.BossBarManager
import dev.jakub.megalodon.dev.jakub.megalodon.bossbar.manager.getBossBarManager
import dev.jakub.megalodon.dev.jakub.megalodon.schedule.time.TaskTimeTypes
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

fun Player.showBossBar(
    text: Component,
    progress: Float = 1.0f,
    builder: BossBarBuilder.() -> Unit = {}
) {
    val bossBarBuilder = createBossBarBuilder(this, text, progress)
    bossBarBuilder.apply(builder)
    bossBarBuilder.show()
}

fun Player.showBossBar(
    text: String,
    progress: Float = 1.0f,
    builder: BossBarBuilder.() -> Unit = {}
) {
    val bossBarBuilder = createBossBarBuilder(this, text, progress)
    bossBarBuilder.apply(builder)
    bossBarBuilder.show()
}

fun Player.hideBossBar(key: String? = null) {
    getBossBarManager().hideBossBar(this, key)
}

fun Player.hideAllBossBars() {
    getBossBarManager().hideAllBossBars(this)
}

fun Player.updateBossBar(
    key: String? = null,
    text: Component? = null,
    progress: Float? = null,
    builder: BossBarBuilder.() -> Unit = {}
) {
    getBossBarManager().updateBossBar(this, key, text, progress, builder)
}

fun Player.hasBossBar(key: String? = null): Boolean {
    return getBossBarManager().hasBossBar(this, key)
}

