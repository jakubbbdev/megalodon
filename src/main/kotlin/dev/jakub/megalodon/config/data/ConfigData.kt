package dev.jakub.megalodon.dev.jakub.megalodon.config.data

data class ConfigOptions(
    val autoSave: Boolean = false,
    val prettyPrint: Boolean = true,
    val serializeNulls: Boolean = true,
    val disableHtmlEscaping: Boolean = false
)

