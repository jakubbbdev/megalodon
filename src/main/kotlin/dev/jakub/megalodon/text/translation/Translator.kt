package dev.jakub.megalodon.dev.jakub.megalodon.text.translation

import net.kyori.adventure.util.Services
import java.util.Locale
import java.util.ResourceBundle

private var fallbackLocale = Locale.ENGLISH

fun setFallbackLocale(locale: Locale) {
    fallbackLocale = locale
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