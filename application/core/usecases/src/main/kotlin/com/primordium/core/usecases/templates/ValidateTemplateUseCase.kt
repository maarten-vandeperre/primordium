package com.primordium.core.usecases.templates

import com.primordium.core.coreutils.functional.Response
import com.primordium.core.domain.templates.Template
import com.primordium.core.domain.templates.TemplateDefinition
import com.primordium.core.usecases.templates.ValidateTemplateUseCase.UseCaseRequest
import com.primordium.core.usecases.templates.ValidateTemplateUseCase.UseCaseResponse

typealias FieldName = String
typealias ErrorMessage = String

data class TemplateValidationResult(
    val success: Boolean,
    val fieldErrors: Map<FieldName, ErrorMessage>
)

interface ValidateTemplateUseCase {
    fun execute(request: UseCaseRequest): Response<UseCaseResponse>

    data class UseCaseRequest(
        val template: Template,
        val templateDefinition: TemplateDefinition
    )

    data class UseCaseResponse(
        val validationResult: TemplateValidationResult
    )
}

class DefaultValidateTemplateUseCase: ValidateTemplateUseCase{
    override fun execute(request: UseCaseRequest): Response<UseCaseResponse> {
        TODO("Not yet implemented")
    }

}