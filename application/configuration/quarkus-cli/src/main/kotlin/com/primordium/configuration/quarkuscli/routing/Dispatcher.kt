package com.primordium.configuration.quarkuscli.routing

import com.primordium.configuration.quarkuscli.commands.Command
import com.primordium.configuration.quarkuscli.commands.NoCommand
import com.primordium.configuration.quarkuscli.commands.Output
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

    fun dispatch(vararg args: String): Output {
        return try {
            val result = getCommand(*args)
                .flatMap {
                    if (getLastArg(*args) == "--help") {
                        success(it.getHelp())
                    } else {
                        it.execute()
                    }
                }
            when (result) {
                is SuccessResponse -> result.data
                is ErrorResponse -> printErrorMessages(result.errorMessages)
                else -> throw IllegalStateException("Result type ${result.javaClass.canonicalName} is not supported.")
            }
        } catch (e: Exception) {
            if (getLastArg(*args) == "--stacktrace") {
                println("Uncaught exception: ${e.localizedMessage}")
                println(e.stackTraceToString())
            }
            """
                Our apologies, something went wrong on our side.
                Please run with --stacktrace and report a bug.
            """.trimIndent()
        }
    }

    private fun getCommand(vararg args: String): Response<Command> {
        val cleanedArgs = args.filterNot { it.startsWith("--") }
        return if (cleanedArgs.isEmpty()) {
            success(commandMap[NoCommand.NAME]!!)
        } else {
            fail("Unknown command for '${args.joinToString(" ")}'. Try running --help.")
        }
    }

    private fun printErrorMessages(errorMessages: List<String>): Output {
        return """
            Command execution failed:
                ${errorMessages.joinToString("\n\t")}    
        """.trimIndent().trim()
    }

    private fun getLastArg(vararg args: String): String? {
        return if (args.isEmpty()) {
            null
        } else {
            args[args.size - 1]
        }
    }
}