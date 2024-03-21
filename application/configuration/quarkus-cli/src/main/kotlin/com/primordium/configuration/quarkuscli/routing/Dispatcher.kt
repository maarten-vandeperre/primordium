package com.primordium.configuration.quarkuscli.routing

import com.primordium.configuration.quarkuscli.commands.*
import com.primordium.core.coreutils.functional.ErrorResponse
import com.primordium.core.coreutils.functional.Response
import com.primordium.core.coreutils.functional.Response.Companion.fail
import com.primordium.core.coreutils.functional.Response.Companion.success
import com.primordium.core.coreutils.functional.SuccessResponse
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance


@ApplicationScoped
class Dispatcher(
    commands: Instance<Command>
) {
    private val commandMap = commands.associateBy { it.name }

    fun dispatch(vararg inputArgs: String): Output {
        val args = inputArgs.toList().map { it.split(Regex(" ")).toList() }.flatten()
        return try {
            val result = getCommand(args)
                .map {
                    if (getLastArg(args) == "--help") {
                        success(it.getHelp())
                    } else {
                        it.execute(getParameters(args))
                    }
                }
            when (result) {
                is SuccessResponse -> result.data
                is ErrorResponse -> printErrorMessages(result.errorMessages)
                else -> throw IllegalStateException("Result type ${result.javaClass.canonicalName} is not supported.")
            }
        } catch (e: Exception) {
            if (getLastArg(args) == "--stacktrace") {
                println("Uncaught exception: ${e.localizedMessage}")
                println(e.stackTraceToString())
            }
            """
                Our apologies, something went wrong on our side.
                Please run with --stacktrace and report a bug.
            """.trimIndent()
        }
    }

    private fun getCommand(args: List<String>): Response<Command> {
        val cleanedArgs = args.filterNot { it.startsWith("--") }
        return if (cleanedArgs.isEmpty()) {
            success(commandMap[NoCommand.NAME]!!)
        } else {
            when (getFirstArg(args)) {
                CreateDefaultTemplateDefinitionCommand.NAME -> success(commandMap[getFirstArg(args)]!!)
                else -> fail("Unknown command for '${args.joinToString(" ")}'. Try running --help.")
            }
        }
    }

    private fun printErrorMessages(errorMessages: List<String>): Output {
        return """
            Command execution failed:
                ${errorMessages.joinToString("\n\t")}    
        """.trimIndent().trim()
    }

    private fun getLastArg(args: List<String>): String? {
        return if (args.isEmpty()) {
            null
        } else {
            args[args.size - 1]
        }
    }

    private fun getFirstArg(args: List<String>): String? {
        return if (args.isEmpty()) {
            null
        } else {
            args[0]
        }
    }

    private fun getParameters(args: List<String>): Map<ParameterKey, ParameterValue> {
        val parameters = args.joinToString(";")
        val matcher = "--([a-zA-Z-]+)=([a-zA-Z-/_.0-9]+)".toRegex()
        return matcher.findAll(parameters).map { it.groupValues[1] to it.groupValues[2] }.toMap()
    }
}