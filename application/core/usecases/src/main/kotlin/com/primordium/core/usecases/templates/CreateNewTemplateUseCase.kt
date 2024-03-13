package com.primordium.core.usecases.templates

import com.primordium.core.coreutils.functional.Response

interface CreateNewTemplateUseCase {
    fun execute(request: UseCaseRequest): Response<UseCaseResponse>

    data class UseCaseRequest(
        val directory: String?
    )

    class UseCaseResponse
}

class DefaultCreateNewTemplateUseCase(
) : CreateNewTemplateUseCase {
    override fun execute(request: CreateNewTemplateUseCase.UseCaseRequest): Response<CreateNewTemplateUseCase.UseCaseResponse> {
        val errors = mutableListOf<String>()
        if (request.directory == null) {
            errors.add("Field 'directory' (path) is required")
        }
        return if (errors.isEmpty()) {
            TODO("start implementing this functionality")
        } else {
            Response.fail(errors)
        }
    }

}