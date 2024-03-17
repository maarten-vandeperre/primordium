package com.primordium.core.usecases.templates

import com.primordium.core.coreutils.functional.ErrorResponse
import com.primordium.core.coreutils.functional.Response
import com.primordium.core.coreutils.functional.Response.Companion.fail
import com.primordium.core.coreutils.functional.Response.Companion.success
import com.primordium.core.domain.templates.File
import com.primordium.core.domain.templates.Template
import com.primordium.core.domain.templates.TemplateDefinition
import com.primordium.core.usecases.templates.ProcessTemplateUseCase.UseCaseRequest
import com.primordium.core.usecases.templates.ProcessTemplateUseCase.UseCaseResponse

interface ProcessTemplateUseCase {
    fun execute(request: UseCaseRequest): Response<UseCaseResponse>

    data class UseCaseRequest(
        val template: Template,
        val templateDefinition: TemplateDefinition
    )

    data class UseCaseResponse(
        val files: List<File>
    )
}

class DefaultProcessTemplateUseCase : ProcessTemplateUseCase {
    override fun execute(request: UseCaseRequest): Response<UseCaseResponse> {
        val validationResult = request.template.validate(request.templateDefinition)

        return if (validationResult is ErrorResponse) {
            fail("Template is not valid: ${validationResult.errorMessages.joinToString("; ")}")
        } else {
            generateFilesForTemplate(request.template).map { UseCaseResponse(files = it) }
        }
    }

    companion object {
        private fun generateFilesForTemplate(template: Template): Response<List<File>> {
            return success(mutableListOf<File>())
                .map { it.addAll(generateDockerComposeFilesForTemplate(template)); it }
        }

        private fun generateDockerComposeFilesForTemplate(template: Template): Response<List<File>> {

        }

        private fun generateHelmFilesForTemplate(template: Template): Response<List<File>> {

        }
    }

}