package dev.jakub.megalodon.dev.jakub.megalodon.config.builder

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.jakub.megalodon.dev.jakub.megalodon.annotations.InternalUse
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import kotlin.reflect.KClass

@InternalUse
inline fun <reified T : Any> createConfigBuilder(
    plugin: Plugin,
    name: String
): ConfigBuilder<T> = ConfigBuilder(plugin, name, T::class)

@InternalUse
class ConfigBuilder<T : Any>(
    private val plugin: Plugin,
    private val name: String,
    private val type: KClass<T>
) {
    private val configFile: File = File(plugin.dataFolder, name)
    private var gsonBuilder: GsonBuilder = GsonBuilder()
    private var gson: Gson = gsonBuilder.create()

    private var autoSave: Boolean = false
    private var defaultConfig: T? = null
    private var customFolder: File? = null
    private var onReload: ((T) -> Unit)? = null
    private var onSave: ((T) -> Unit)? = null
    private var onLoad: ((T) -> Unit)? = null
    private var validator: ((T) -> Boolean)? = null

    fun autoSave(enabled: Boolean = true) {
        autoSave = enabled
    }

    fun defaultConfig(config: T) {
        defaultConfig = config
    }

    fun folder(folder: File) {
        customFolder = folder
    }

    fun folder(folderName: String) {
        customFolder = File(plugin.dataFolder, folderName)
    }

    fun prettyPrint(enabled: Boolean = true) {
        if (enabled) {
            gsonBuilder.setPrettyPrinting()
        }
        rebuildGson()
    }

    fun serializeNulls(enabled: Boolean = true) {
        if (enabled) {
            gsonBuilder.serializeNulls()
        }
        rebuildGson()
    }

    fun disableHtmlEscaping(enabled: Boolean = true) {
        if (enabled) {
            gsonBuilder.disableHtmlEscaping()
        }
        rebuildGson()
    }

    fun customGson(gson: Gson) {
        this.gson = gson
    }

    fun customGson(builder: GsonBuilder.() -> Unit) {
        gsonBuilder.apply(builder)
        rebuildGson()
    }

    fun onReload(callback: (T) -> Unit) {
        onReload = callback
    }

    fun onSave(callback: (T) -> Unit) {
        onSave = callback
    }

    fun onLoad(callback: (T) -> Unit) {
        onLoad = callback
    }

    fun validate(validator: (T) -> Boolean) {
        this.validator = validator
    }

    private fun rebuildGson() {
        gson = gsonBuilder.create()
    }

    private fun getConfigFile(): File {
        return if (customFolder != null) {
            customFolder!!.mkdirs()
            File(customFolder, name)
        } else {
            File(plugin.dataFolder, name)
        }
    }

    fun build(): T {
        val file = getConfigFile()
        
        if (!file.exists()) {
            file.parentFile.mkdirs()
            if (defaultConfig != null) {
                val config = defaultConfig!!
                if (validator?.invoke(config) == false) {
                    throw IllegalStateException("Default config failed validation for ${file.name}")
                }
                save(config)
                onLoad?.invoke(config)
                return config
            }
            throw IllegalStateException("Config file ${file.name} does not exist and no default config provided")
        }

        val loaded = load()
        if (loaded != null) {
            if (validator?.invoke(loaded) == false) {
                plugin.logger.warning("Config ${file.name} failed validation, using default if available")
                if (defaultConfig != null) {
                    val config = defaultConfig!!
                    save(config)
                    onLoad?.invoke(config)
                    return config
                }
                throw IllegalStateException("Config ${file.name} failed validation and no default config provided")
            }
            onLoad?.invoke(loaded)
            return loaded
        }

        if (defaultConfig != null) {
            val config = defaultConfig!!
            if (validator?.invoke(config) == false) {
                throw IllegalStateException("Default config failed validation for ${file.name}")
            }
            save(config)
            onLoad?.invoke(config)
            return config
        }

        throw IllegalStateException("Failed to load config ${file.name} and no default config provided")
    }

    fun load(): T? {
        val file = getConfigFile()
        
        if (!file.exists()) {
            return null
        }

        return try {
            FileReader(file).use { reader ->
                gson.fromJson(reader, type.java)
            }
        } catch (e: Exception) {
            plugin.logger.severe("Failed to load config ${file.name}: ${e.message}")
            null
        }
    }

    fun save(data: T) {
        if (validator?.invoke(data) == false) {
            throw IllegalStateException("Config data failed validation")
        }
        
        val file = getConfigFile()
        file.parentFile.mkdirs()
        
        FileWriter(file).use { writer ->
            gson.toJson(data, writer)
        }
        
        onSave?.invoke(data)
    }

    fun reload(): T? {
        val loaded = load()
        loaded?.let { 
            if (validator?.invoke(it) == false) {
                plugin.logger.warning("Reloaded config ${getConfigFile().name} failed validation")
                return null
            }
            onReload?.invoke(it)
        }
        return loaded
    }

    fun exists(): Boolean {
        return getConfigFile().exists()
    }

    fun delete() {
        val file = getConfigFile()
        if (file.exists()) {
            file.delete()
        }
    }
}

