package dev.jakub.megalodon.dev.jakub.megalodon.bossbar.manager

import dev.jakub.megalodon.dev.jakub.megalodon.annotations.InternalUse
import dev.jakub.megalodon.dev.jakub.megalodon.bossbar.builder.BossBarBuilder
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@InternalUse
private val bossBarRegistry: MutableMap<UUID, MutableMap<String?, BossBar>> = ConcurrentHashMap()

@InternalUse
fun getBossBarManager(): BossBarManager = BossBarManager

@InternalUse
object BossBarManager {
    fun showBossBar(player: Player, bossBar: BossBar, key: String? = null) {
        val playerBossBars = bossBarRegistry.computeIfAbsent(player.uniqueId) { ConcurrentHashMap() }
        
        key?.let { playerBossBars[it]?.let { oldBossBar -> player.hideBossBar(oldBossBar) } }
        
        playerBossBars[key] = bossBar
        player.showBossBar(bossBar)
    }

    fun hideBossBar(player: Player, key: String? = null) {
        val playerBossBars = bossBarRegistry[player.uniqueId] ?: return
        
        if (key != null) {
            playerBossBars[key]?.let { bossBar ->
                player.hideBossBar(bossBar)
                playerBossBars.remove(key)
            }
        } else {
            playerBossBars.values.firstOrNull()?.let { bossBar ->
                player.hideBossBar(bossBar)
                playerBossBars.remove(playerBossBars.entries.firstOrNull()?.key)
            }
        }
        
        if (playerBossBars.isEmpty()) {
            bossBarRegistry.remove(player.uniqueId)
        }
    }

    fun hideAllBossBars(player: Player) {
        val playerBossBars = bossBarRegistry[player.uniqueId] ?: return
        
        playerBossBars.values.forEach { bossBar ->
            player.hideBossBar(bossBar)
        }
        
        bossBarRegistry.remove(player.uniqueId)
    }

    fun updateBossBar(
        player: Player,
        key: String? = null,
        text: Component? = null,
        progress: Float? = null,
        builder: BossBarBuilder.() -> Unit = {}
    ) {
        val playerBossBars = bossBarRegistry[player.uniqueId] ?: return
        val bossBar = if (key != null) {
            playerBossBars[key]
        } else {
            playerBossBars.values.firstOrNull()
        } ?: return

        text?.let { bossBar.name(it) }
        progress?.let { bossBar.progress(it.coerceIn(0f, 1f)) }
    }

    fun hasBossBar(player: Player, key: String? = null): Boolean {
        val playerBossBars = bossBarRegistry[player.uniqueId] ?: return false
        
        return if (key != null) {
            playerBossBars.containsKey(key)
        } else {
            playerBossBars.isNotEmpty()
        }
    }

    fun getBossBar(player: Player, key: String? = null): BossBar? {
        val playerBossBars = bossBarRegistry[player.uniqueId] ?: return null
        
        return if (key != null) {
            playerBossBars[key]
        } else {
            playerBossBars.values.firstOrNull()
        }
    }
}

