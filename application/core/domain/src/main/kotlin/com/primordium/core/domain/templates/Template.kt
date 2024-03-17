package com.primordium.core.domain.templates

import com.primordium.core.coreutils.functional.Response
import com.primordium.core.coreutils.functional.Response.Companion.success
import com.primordium.core.coreutils.functional.SuccessResponse
import com.primordium.core.coreutils.functional.Validation
import com.primordium.core.coreutils.functional.ValidationUtils

typealias FieldName = String
typealias ErrorMessage = String

data class Template(
    val name: String,
    val version: String,
    val fields: List<TemplateField> = emptyList()
) {
    fun validate(definition: TemplateDefinition): Response<TemplateValidationResult> {
        val fieldErrors = mutableMapOf<FieldName, ErrorMessage>()

        val validationResult = ValidationUtils.validateIf(
            Validation(
                { this.name == definition.name },
                "Template name and template definition name are not matching: '${this.name}' vs '${definition.name}'"
            ) { fieldErrors[".name"] = it }
        )

        return success(
            TemplateValidationResult(
                success = validationResult is SuccessResponse,
                fieldErrors = fieldErrors
            )
        )
    }
}

data class TemplateValidationResult(
    val success: Boolean,
    val fieldErrors: Map<FieldName, ErrorMessage>
)

data class TemplateField(
    val name: String,
    val value: String,
    val subFields: List<TemplateField> = emptyList()
)