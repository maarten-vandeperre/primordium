package com.primordium.configuration.quarkuscli.commands

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.primordium.core.coreutils.functional.Response
import com.primordium.core.usecases.templates.*
import jakarta.enterprise.context.ApplicationScoped
import org.yaml.snakeyaml.DumperOptions
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths


@ApplicationScoped
class CreateDefaultTemplateDefinitionCommand(
    private val createDefaultTemplateDefinitionUseCase: CreateDefaultTemplateDefinitionUseCase
) : Command {
    override val name: Output
        get() = NAME
    override val description: Output
        get() = SHORT_DESCRIPTION
    override val category: CommandCategory
        get() = CommandCategory.BASIC_COMMAND

    override fun execute(parameters: Map<ParameterKey, ParameterValue>): Response<Output> {
        val fileName = parameters.getOrDefault("file-name", DEFAULT_FILE_NAME)
        return if (!parameters.containsKey("directory")) {
            Response.fail("No directory parameter is given, try ' --help' to get more information")
        } else {
            val file = Files.createFile(Paths.get("${parameters["directory"]}/$fileName"))
            createDefaultTemplateDefinitionUseCase.execute(
                CreateDefaultTemplateDefinitionUseCase.UseCaseRequest(TEMPLATE_FIELD_SELECTION)
            ).mapData { response ->
                PrintWriter(file.toFile()).use { writer ->
                    val builder = YAMLFactory.builder()
                    val dumperOptions = DumperOptions()
                    dumperOptions.width = 200
                    builder.dumperOptions(dumperOptions)
                    val factory = builder.build()
                    val mapper = ObjectMapper(factory)
                    val yaml = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.templateDefinition)
                    writer.write(yaml)
                }
                "Generated the default template definition (${file.toAbsolutePath()})."
            }
        }
    }

    override fun getHelp(): Output {
        return """
            $SHORT_DESCRIPTION
            
            Parameters (--parameter=...):
            *: required parameter
                directory*      Absolute or relative path to the directory where the file will be written.
                file-name       Override of the default file name (default = $DEFAULT_FILE_NAME).
        """.trimIndent()
    }

    companion object {
        const val NAME = "default-template-definition"
        const val DEFAULT_FILE_NAME = "default-primordium-template.yaml"
        const val SHORT_DESCRIPTION = "Creates a new default template definition."
        private val TEMPLATE_FIELD_SELECTION = TemplateFieldSelection(
            selection = TemplateDefinitionSelection(
                metadata = TemplateFieldSelectionItemWithSubFields(
                    enabled = true,
                    subFields = TemplateDefinitionSelectionForMetaData(
                        name = TemplateFieldSelectionItemWithoutSubFields(
                            enabled = true
                        ),
                        namespace = TemplateFieldSelectionItemWithoutSubFields(
                            enabled = true
                        ),
                        port = TemplateFieldSelectionItemWithoutSubFields(
                            enabled = true
                        ),
                        instanceCount = TemplateFieldSelectionItemWithoutSubFields(
                            enabled = true
                        ),
                        image = TemplateFieldSelectionItemWithoutSubFields(
                            enabled = true
                        )
                    )
                ),
                applicationChecks = TemplateFieldSelectionItemWithSubFields(
                    enabled = true,
                    subFields = TemplateDefinitionSelectionForApplicationChecks(
                        heartBeatCheck = TemplateFieldSelectionItemWithoutSubFields(
                            enabled = true
                        ),
                        consciousCheck = TemplateFieldSelectionItemWithoutSubFields(
                            enabled = true
                        ),
                        birthCheck = TemplateFieldSelectionItemWithoutSubFields(
                            enabled = true
                        )
                    )
                )
            )
        )
    }

}