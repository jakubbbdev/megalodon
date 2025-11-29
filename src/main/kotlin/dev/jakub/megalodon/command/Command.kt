package dev.jakub.megalodon.dev.jakub.megalodon.command

import dev.jakub.megalodon.dev.jakub.megalodon.annotations.InternalUse

@InternalUse
val megalodonCommandList: MutableList<MegalodonCommand> = mutableListOf()

fun registerCommand(command: MegalodonCommand) {
    megalodonCommandList.add(command)
}