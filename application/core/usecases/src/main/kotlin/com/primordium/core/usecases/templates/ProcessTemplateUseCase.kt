package com.primordium.core.usecases.templates

import com.primordium.core.coreutils.functional.ErrorResponse
import com.primordium.core.coreutils.functional.Response
import com.primordium.core.coreutils.functional.Response.Companion.fail
import com.primordium.core.coreutils.functional.Response.Companion.flatten
import com.primordium.core.coreutils.functional.Response.Companion.success
import com.primordium.core.domain.templates.*
import com.primordium.core.usecases.templates.ProcessTemplateUseCase.UseCaseRequest
import com.primordium.core.usecases.templates.ProcessTemplateUseCase.UseCaseResponse

data class ContentPlaceholder(
    val name: String,
    val placeHolder: String
)

data class FilledContent(
    val name: String,
    val content: Response<String>
)

interface ProcessTemplateUseCase {
    fun execute(request: UseCaseRequest): Response<UseCaseResponse>

    data class UseCaseRequest(
        val template: Template,
        val templateDefinition: TemplateDefinition,
        val contentPlaceholders: List<ContentPlaceholder>
    )

    data class UseCaseResponse(
        val filledContent: List<FilledContent>
    )
}

class DefaultProcessTemplateUseCase : ProcessTemplateUseCase {
    override fun execute(request: UseCaseRequest): Response<UseCaseResponse> {
        val validationResult = request.template.validate(request.templateDefinition)

        return if (validationResult is ErrorResponse) {
            fail("Template is not valid: ${validationResult.errorMessages.joinToString("; ")}")
        } else {
            success(UseCaseResponse(
                filledContent = request.contentPlaceholders.map {
                    FilledContent(
                        name = it.name,
                        content = fillPlaceholder(it.placeHolder, request.template)
                    )
                }
            ))
        }
    }

    companion object {
        fun listPossibleProperties(template: Template): Response<Map<FieldName, FieldValue>> {
            return listPossibleProperties(template.fields, "")
        }

        private fun listPossibleProperties(fields: List<TemplateField>, parentalPrefix: String): Response<Map<FieldName, FieldValue>> {
            return flatten(
                fields.map { field ->
                    if (field.subFields.isEmpty()) {
                        success(mapOf("${parentalPrefix}${field.name}" to field.value))
                    } else {
                        listPossibleProperties(field.subFields, "${field.name}.")
                    }
                }
            )
                .mapData { it.flatMap { it.entries }.associate { it.key to it.value } }
        }

        fun fillPlaceholder(placeholder: String, template: Template): Response<String> {
            val matcher = "<<(.*)>>".toRegex()
            val extractedProperties = matcher.findAll(placeholder).map { it.groupValues[1] }.toSet()
            return listPossibleProperties(template)
                .map { templateProperties ->
                    flatten(extractedProperties.map { validateProperty(it, templateProperties.keys) }).mapData { templateProperties } //TODO construct can be abstracted
                }
                .mapData { templateProperties ->
                    var result = placeholder
                    extractedProperties.forEach { extractedProperty ->
                        result = result.replace(Regex("<<${extractedProperty.replace(Regex("\\."), "\\.")}>>"), templateProperties[extractedProperty]!!)
                    }
                    result
                }
        }

        private fun validateProperty(placeholderProperty: String, supportedFieldProperties: Set<String>): Response<Boolean> {
            return if (supportedFieldProperties.contains(placeholderProperty)) {
                success(true)
            } else {
                fail("Placeholder property '${placeholderProperty}' is not supported at the moment. Supported properties are: ${supportedFieldProperties.joinToString(",")}")
            }
        }
    }

}