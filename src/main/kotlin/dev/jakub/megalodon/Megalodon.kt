package dev.jakub.megalodon.dev.jakub.megalodon

import org.bukkit.plugin.java.JavaPlugin

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
lateinit var MegalodonInstance: Megalodon
    private set

abstract class Megalodon : JavaPlugin() {

    open fun load() {}

    open fun enable() {}

    open fun disable() {}

    override fun onLoad() {
        load()
    }

    override fun onEnable() {
        MegalodonInstance = this
        enable()
        registerCommands()
    }

    override fun onDisable() {
        disable()
    }

    private fun registerCommands() {

    }
}