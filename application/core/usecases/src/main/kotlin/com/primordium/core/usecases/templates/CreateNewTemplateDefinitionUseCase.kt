package com.primordium.core.usecases.templates

import com.primordium.core.coreutils.functional.Response
import com.primordium.core.coreutils.functional.Response.Companion.success
import com.primordium.core.domain.templates.TemplateDefinition
import com.primordium.core.domain.templates.TemplateDefinitionField

interface CreateNewTemplateDefinitionUseCase {
    fun execute(request: UseCaseRequest): Response<UseCaseResponse>

    data class UseCaseRequest(
        val templateFieldSelection: TemplateFieldSelection
    )

    data class UseCaseResponse(
        val templateDefinition: TemplateDefinition
    )
}

class DefaultCreateNewTemplateDefinitionUseCase(
) : CreateNewTemplateDefinitionUseCase {
    override fun execute(request: CreateNewTemplateDefinitionUseCase.UseCaseRequest): Response<CreateNewTemplateDefinitionUseCase.UseCaseResponse> {
        return success(
            CreateNewTemplateDefinitionUseCase.UseCaseResponse(
                templateDefinition = TemplateDefinition(
                    name = "Primordium Template Defintion",
                    description = """
                    A version of the template to configure and validate for Primordium's use (i.e., generation of deployment files).
                """.trimIndent(),
                    version = "0.0.1",
                    fields = listOfNotNull(
                        request.templateFieldSelection.ifEnabled(applicationChecks(request.templateFieldSelection))
                    )
                )
            )
        )
    }
}

private fun applicationChecks(templateFieldSelection: TemplateFieldSelection) = TemplateDefinitionField(
    name = "application-checks",
    description = "Checks that need to be implemented to define the overall health and responsiveness of the deployed application.",
    required = true,
    subFields = listOfNotNull(
        templateFieldSelection.ifEnabled(heartBeatCheck())
    )
)

private fun heartBeatCheck() = TemplateDefinitionField(
    name = "heart-beat-checks",
    description = "Checks that the application started.",
    required = true
)

data class TemplateFieldSelection(
    val selection: TemplateDefinitionSelection
) {
    fun ifEnabled(field: TemplateDefinitionField): TemplateDefinitionField? {
        val isEnabled = when (field.name) {
            "application-checks" -> selection.applicationChecks.enabled
            "heart-beat-checks" -> selection.applicationChecks.subFields.heartBeatCheck.enabled
            "conscious-checks" -> selection.applicationChecks.subFields.consciousCheck.enabled
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
    val applicationChecks: TemplateFieldSelectionItemWithSubFields<TemplateDefinitionSelectionForApplicationChecks>
)

data class TemplateDefinitionSelectionForApplicationChecks(
    val heartBeatCheck: TemplateFieldSelectionItemWithoutSubFields,
    val consciousCheck: TemplateFieldSelectionItemWithoutSubFields
)