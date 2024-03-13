package com.primordium.configuration.quarkuscli.commands

import com.primordium.core.coreutils.functional.Response

typealias Output = String

interface Command {
    val name: Output
    val description: Output
    val category: CommandCategory

    fun execute(): Response<Output>
    fun getHelp(): Output
}

enum class CommandCategory {
    NONE, BASIC_COMMAND
}