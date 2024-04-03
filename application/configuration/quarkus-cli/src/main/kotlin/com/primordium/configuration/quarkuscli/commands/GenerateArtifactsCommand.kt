package com.primordium.configuration.quarkuscli.commands

import com.primordium.configuration.quarkuscli.utils.TemplateDefinitionUtils.toTemplateDefinition
import com.primordium.configuration.quarkuscli.utils.TemplateUtils.toTemplate
import com.primordium.core.coreutils.functional.ErrorResponse
import com.primordium.core.coreutils.functional.Response
import com.primordium.core.coreutils.functional.Response.Companion.success
import com.primordium.core.coreutils.functional.SuccessResponse
import com.primordium.core.domain.templates.Template
import com.primordium.core.domain.templates.TemplateDefinition
import com.primordium.core.usecases.templates.ContentPlaceholder
import com.primordium.core.usecases.templates.ProcessTemplateUseCase
import jakarta.enterprise.context.ApplicationScoped
import java.io.File
import java.lang.Thread.currentThread
import java.nio.file.Files
import java.nio.file.Paths


@ApplicationScoped
class GenerateArtifactsCommand(
    private val processTemplateUseCase: ProcessTemplateUseCase
) : Command {
    override val name: Output
        get() = NAME
    override val description: Output
        get() = SHORT_DESCRIPTION
    override val category: CommandCategory
        get() = CommandCategory.BASIC_COMMAND

    @Suppress("UNCHECKED_CAST")
    override fun execute(parameters: Map<ParameterKey, ParameterValue>): Response<Output> { //TODO for now, output folders get created if they don't exist
        return if (!parameters.containsKey("directory")) { //TODO directory exists & extract shared logic //TODO check folder is empty
            Response.fail("No directory parameter is given, try ' --help' to get more information")
        } else {
            val contentPlaceholders = listDefaultFiles()
            val ctxl = currentThread().contextClassLoader
            val templateResponse =
                toTemplate(String(ctxl.getResourceAsStream("META-INF/default-files/default-primordium-template.yaml")!!.readAllBytes()))
            val templateDefinitionResponse =
                toTemplateDefinition(String(ctxl.getResourceAsStream("META-INF/default-files/default-primordium-template-definition.yaml")!!.readAllBytes()))

            Response
                .flatten(listOf(templateResponse, templateDefinitionResponse, contentPlaceholders))
                .map { responses ->
                    processTemplateUseCase.execute(
                        ProcessTemplateUseCase.UseCaseRequest(
                            template = responses[0] as Template,
                            templateDefinition = responses[1] as TemplateDefinition,
                            contentPlaceholders = responses[2] as List<ContentPlaceholder>
                        )
                    )
                }
                .mapData {
                    it.filledContent.map { "${parameters["directory"]}/${it.name}".replaceAfterLast("/", "") }.forEach { File(it).mkdirs() }
                    it.filledContent.filter { it.content is SuccessResponse }.forEach {
                        Files.writeString(Paths.get("${parameters["directory"]}/${it.name}"), (it.content as SuccessResponse<String>).data)
                    }

                    """
                        Generated the following files:
                        ${
                        it.filledContent
                            .filter { it.content is SuccessResponse }
                            .joinToString(";\n", "    ") { "${parameters["directory"]}${it.name}" }
                    }
                        
                        Couldn't generate the following files:
                        ${
                        it.filledContent
                            .filter { it.content is ErrorResponse }
                            .joinToString(";\n", "    ") { "${parameters["directory"]}${it.name}: ${(it.content as ErrorResponse).errorMessages.joinToString(";")}" }
                    }
                    """.trimIndent()
                }
        }
    }

    private fun listDefaultFiles(): Response<List<ContentPlaceholder>> {
        val resource = currentThread().contextClassLoader.getResourceAsStream("META-INF/default-artefacts/.primordium-default-artefacts.txt")!!
        val placeholderFiles = String(resource.readAllBytes()).split(Regex("\n\r?")).toList().filterNot { it.isBlank() }
        return success(
            placeholderFiles
                .map {
                    ContentPlaceholder(
                        name = it,
                        placeHolder = String(currentThread().contextClassLoader.getResourceAsStream("META-INF/default-artefacts${it}")!!.readAllBytes())
                    )
                }
        )
    }

    override fun getHelp(): Output {
        return """
            $SHORT_DESCRIPTION
            
            Parameters (--parameter=...):
            *: required parameter
                directory*      Absolute or relative path to the directory where the artifacts will be written.
        """.trimIndent()
    }

    companion object {
        const val NAME = "generate-artifacts"
        const val SHORT_DESCRIPTION = "Generates the artifacts like (docker-/podman-)compose files, Kubernetes deployment files, ...."
    }

}