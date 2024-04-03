package com.primordium.configuration.quarkuscli.config

import com.primordium.core.usecases.templates.CreateDefaultTemplateDefinitionUseCase
import com.primordium.core.usecases.templates.DefaultCreateDefaultTemplateDefinitionUseCase
import com.primordium.core.usecases.templates.DefaultProcessTemplateUseCase
import com.primordium.core.usecases.templates.ProcessTemplateUseCase
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces

@ApplicationScoped
class UseCaseConfig {
    @Produces
    fun createDefaultTemplateDefinitionUseCase(): CreateDefaultTemplateDefinitionUseCase {
        return DefaultCreateDefaultTemplateDefinitionUseCase()
    }

    @Produces
    fun processTemplateUseCase(): ProcessTemplateUseCase {
        return DefaultProcessTemplateUseCase()
    }
}