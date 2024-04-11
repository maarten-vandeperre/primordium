package com.primordium.core.usecases.templates

import com.primordium.core.coreutils.functional.Response
import com.primordium.core.coreutils.functional.Response.Companion.success
import com.primordium.core.domain.templates.TemplateDefinition
import com.primordium.core.domain.templates.TemplateDefinitionField

//TODO set default values
//TODO support integer and boolean values as well
interface CreateDefaultTemplateDefinitionUseCase {
    fun execute(request: UseCaseRequest): Response<UseCaseResponse>

    data class UseCaseRequest(
        val templateFieldSelection: TemplateFieldSelection
    )

    data class UseCaseResponse(
        val templateDefinition: TemplateDefinition
    )
}

class DefaultCreateDefaultTemplateDefinitionUseCase(
) : CreateDefaultTemplateDefinitionUseCase {
    override fun execute(request: CreateDefaultTemplateDefinitionUseCase.UseCaseRequest): Response<CreateDefaultTemplateDefinitionUseCase.UseCaseResponse> {
        return success(
            CreateDefaultTemplateDefinitionUseCase.UseCaseResponse(
                templateDefinition = TemplateDefinition(
                    name = "Primordium Template",
                    description = """
                    A version of the template to configure and validate for Primordium's use (i.e., generation of deployment files).
                """.trimIndent(),
                    version = "0.0.1",
                    fields = listOfNotNull(
                        request.templateFieldSelection.ifEnabled(metaData(request.templateFieldSelection)),
                        request.templateFieldSelection.ifEnabled(applicationChecks(request.templateFieldSelection))
                    )
                )
            )
        )
    }
}

private fun metaData(templateFieldSelection: TemplateFieldSelection) = TemplateDefinitionField(
    name = "metadata",
    description = "Metadata configurations in order to get the application deployed and to make the application accessible.",
    required = true,
    subFields = listOfNotNull(
        templateFieldSelection.ifEnabled(nameMetaData()),
        templateFieldSelection.ifEnabled(namespaceMetaData()),
        templateFieldSelection.ifEnabled(portMetaData()),
        templateFieldSelection.ifEnabled(instanceCountMetaData()),
        templateFieldSelection.ifEnabled(imageMetaData())
    )
)

private fun nameMetaData() = TemplateDefinitionField(
    name = "name",
    description = "The name of the application that will get deployed.",
    required = true
)

private fun namespaceMetaData() = TemplateDefinitionField(
    name = "name",
    description = "The namespace in which the application that will get deployed.",
    required = true
)

private fun portMetaData() = TemplateDefinitionField(
    name = "port",
    description = "The port on which the application will be accessible.",
    required = true
)

private fun instanceCountMetaData() = TemplateDefinitionField(
    name = "instance-count",
    description = "The amount of instances of the specified application that need to run.",
    required = true
)

private fun imageMetaData() = TemplateDefinitionField(
    name = "image",
    description = "The reference of the podman/docker/... image that will be deployed.",
    required = true
)

private fun applicationChecks(templateFieldSelection: TemplateFieldSelection) = TemplateDefinitionField(
    name = "application-checks",
    description = "Checks that need to be implemented to define the overall health and responsiveness of the deployed application.",
    required = true,
    subFields = listOfNotNull(
        templateFieldSelection.ifEnabled(heartBeatCheck()),
        templateFieldSelection.ifEnabled(consciousCheck()),
        templateFieldSelection.ifEnabled(birthCheck())
    )
)

private fun heartBeatCheck() = TemplateDefinitionField(
    name = "heart-beat-check",
    description = "Checks that the application started and is active (i.e., no unrecoverable application failure like a dead lock occurred).",
    required = true
)

private fun consciousCheck() = TemplateDefinitionField(
    name = "conscious-check",
    description = "Checks that the application is conscious (i.e., responding in a timely manner).",
    required = true
)

private fun birthCheck() = TemplateDefinitionField(
    name = "birth-check",
    description = "Checks that the application is started (i.e., has a heart beat, but this check can have a different polling/checking interval).",
    required = true
)

data class TemplateFieldSelection(
    val selection: TemplateDefinitionSelection
) {
    fun ifEnabled(field: TemplateDefinitionField): TemplateDefinitionField? {
        val isEnabled = when (field.name) {
            "metadata" -> selection.metadata.enabled
            "name" -> selection.metadata.subFields.name.enabled
            "namespace" -> selection.metadata.subFields.namespace.enabled
            "port" -> selection.metadata.subFields.port.enabled
            "instance-count" -> selection.metadata.subFields.instanceCount.enabled
            "image" -> selection.metadata.subFields.image.enabled
            "application-checks" -> selection.applicationChecks.enabled
            "heart-beat-check" -> selection.applicationChecks.subFields.heartBeatCheck.enabled
            "conscious-check" -> selection.applicationChecks.subFields.consciousCheck.enabled
            "birth-check" -> selection.applicationChecks.subFields.birthCheck.enabled
            else -> throw IllegalStateException("It should not be possible to check enablement of unsupported field '${field.name}'")
        }
        return if (isEnabled) field else null
    }
}

data class TemplateFieldSelectionItemWithSubFields<SUB_FIELDS_TYPE>(
    val enabled: Boolean,
    val subFields: SUB_FIELDS_TYPE
)

data class TemplateFieldSelectionItemWithoutSubFields(
    val enabled: Boolean
)

data class TemplateDefinitionSelection(
    val metadata: TemplateFieldSelectionItemWithSubFields<TemplateDefinitionSelectionForMetaData>,
    val applicationChecks: TemplateFieldSelectionItemWithSubFields<TemplateDefinitionSelectionForApplicationChecks>
)

data class TemplateDefinitionSelectionForApplicationChecks(
    val heartBeatCheck: TemplateFieldSelectionItemWithoutSubFields,
    val consciousCheck: TemplateFieldSelectionItemWithoutSubFields,
    val birthCheck: TemplateFieldSelectionItemWithoutSubFields
)

data class TemplateDefinitionSelectionForMetaData(
    val name: TemplateFieldSelectionItemWithoutSubFields,
    val namespace: TemplateFieldSelectionItemWithoutSubFields,
    val port: TemplateFieldSelectionItemWithoutSubFields,
    val instanceCount: TemplateFieldSelectionItemWithoutSubFields,
    val image: TemplateFieldSelectionItemWithoutSubFields
)