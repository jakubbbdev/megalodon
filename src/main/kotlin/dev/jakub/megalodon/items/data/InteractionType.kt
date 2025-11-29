package dev.jakub.megalodon.dev.jakub.megalodon.items.data

import dev.jakub.megalodon.dev.jakub.megalodon.ux.color.Colorization
import org.bukkit.Sound

enum class InteractionType(val color: Colorization, val sound: Sound) {
    SUCCESS(Colorization.LIME, Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    ERROR(Colorization.RED, Sound.BLOCK_ANVIL_BREAK),
    ENABLED(Colorization.LIME, Sound.ENTITY_VILLAGER_YES),
    DISABLED(Colorization.RED, Sound.ENTITY_VILLAGER_NO),
    CLICK(Colorization.GRAY, Sound.UI_LOOM_SELECT_PATTERN),
    PAGE_TURN(Colorization.GRAY, Sound.UI_LOOM_TAKE_RESULT),

    DISPLAY_CLICK(Colorization.LIME, Sound.UI_LOOM_SELECT_PATTERN)
}