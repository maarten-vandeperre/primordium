package com.primordium.configuration.quarkuscli.config

import com.primordium.core.usecases.templates.CreateDefaultTemplateDefinitionUseCase
import com.primordium.core.usecases.templates.DefaultCreateDefaultTemplateDefinitionUseCase
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces

@ApplicationScoped
class UseCaseConfig {
    @Produces
    fun createDefaultTemplateDefinitionUseCase(): CreateDefaultTemplateDefinitionUseCase {
        return DefaultCreateDefaultTemplateDefinitionUseCase()
    }
}