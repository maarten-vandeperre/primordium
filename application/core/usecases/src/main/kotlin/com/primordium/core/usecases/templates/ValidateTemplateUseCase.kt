package com.primordium.core.usecases.templates

import com.primordium.core.coreutils.functional.Response
import com.primordium.core.domain.templates.Template
import com.primordium.core.domain.templates.TemplateDefinition
import com.primordium.core.domain.templates.TemplateValidationResult
import com.primordium.core.usecases.templates.ProcessTemplateUseCase.UseCaseRequest
import com.primordium.core.usecases.templates.ProcessTemplateUseCase.UseCaseResponse

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

class DefaultValidateTemplateUseCase : ProcessTemplateUseCase {
    override fun execute(request: UseCaseRequest): Response<UseCaseResponse> {
        return request.template
            .validate(request.templateDefinition)
            .map { UseCaseResponse(validationResult = it) }
    }

}