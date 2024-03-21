package com.primordium.configuration.quarkuscli.commands

import com.primordium.core.coreutils.functional.Response

typealias Output = String
typealias ParameterKey = String
typealias ParameterValue = String

interface Command {
    val name: Output
    val description: Output
    val category: CommandCategory

    fun execute(parameters: Map<ParameterKey, ParameterValue>): Response<Output>
    fun getHelp(): Output
}

enum class CommandCategory {
    NONE, BASIC_COMMAND
}