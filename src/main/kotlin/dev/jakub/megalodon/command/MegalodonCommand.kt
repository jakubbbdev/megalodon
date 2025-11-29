package dev.jakub.megalodon.dev.jakub.megalodon.command

import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack

data class MegalodonCommand(
    val command: LiteralCommandNode<CommandSourceStack>,
    val description: String,
    val aliases: List<String>,
)