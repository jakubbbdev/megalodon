package dev.jakub.megalodon.dev.jakub.megalodon.config

import dev.jakub.megalodon.dev.jakub.megalodon.Megalodon
import dev.jakub.megalodon.dev.jakub.megalodon.config.builder.ConfigBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.config.builder.createConfigBuilder
import org.bukkit.plugin.Plugin
import java.io.File

inline fun <reified T : Any> Megalodon.loadConfig(
    name: String = "config.json",
    crossinline builder: ConfigBuilder<T>.() -> Unit = {}
): T = (this as Plugin).loadConfig<T>(name, builder)

inline fun <reified T : Any> Plugin.loadConfig(
    name: String = "config.json",
    crossinline builder: ConfigBuilder<T>.() -> Unit = {}
): T {
    val configBuilder = createConfigBuilder<T>(this, name)
    configBuilder.apply(builder)
    return configBuilder.build()
}

inline fun <reified T : Any> Plugin.saveConfig(
    name: String = "config.json",
    data: T,
    crossinline builder: ConfigBuilder<T>.() -> Unit = {}
) {
    val configBuilder = createConfigBuilder<T>(this, name)
    configBuilder.apply(builder)
    configBuilder.save(data)
}

inline fun <reified T : Any> Plugin.getConfig(
    name: String = "config.json",
    crossinline builder: ConfigBuilder<T>.() -> Unit = {}
): T? {
    val configBuilder = createConfigBuilder<T>(this, name)
    configBuilder.apply(builder)
    return configBuilder.load()
}

inline fun <reified T : Any> Plugin.reloadConfig(
    name: String = "config.json",
    crossinline builder: ConfigBuilder<T>.() -> Unit = {}
): T? {
    val configBuilder = createConfigBuilder<T>(this, name)
    configBuilder.apply(builder)
    return configBuilder.reload()
}

inline fun <reified T : Any> Plugin.configExists(
    name: String = "config.json"
): Boolean {
    val configBuilder = createConfigBuilder<T>(this, name)
    return configBuilder.exists()
}

inline fun <reified T : Any> Plugin.deleteConfig(
    name: String = "config.json"
) {
    val configBuilder = createConfigBuilder<T>(this, name)
    configBuilder.delete()
}

