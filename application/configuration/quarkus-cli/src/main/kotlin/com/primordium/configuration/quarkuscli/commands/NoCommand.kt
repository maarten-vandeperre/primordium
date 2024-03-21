package com.primordium.configuration.quarkuscli.commands

import com.primordium.core.coreutils.functional.Response
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class NoCommand : Command {
    override val name: Output
        get() = NAME
    override val description: Output
        get() = ""
    override val category: CommandCategory
        get() = CommandCategory.NONE

    override fun execute(parameters: Map<ParameterKey, ParameterValue>): Response<Output> {
        return Response.fail("No parameters are given, try ' --help' to get more information")
    }

    override fun getHelp(): Output {
        return """
            Primordium is a template engine to ....
            
            Basic commands:
                ${CreateDefaultTemplateDefinitionCommand.NAME}        ${CreateDefaultTemplateDefinitionCommand.SHORT_DESCRIPTION}
        """.trimIndent()
    }

    companion object {
        const val NAME = "no-command"
    }

}