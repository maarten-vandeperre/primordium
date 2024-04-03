package com.primordium.utilities

import com.primordium.configuration.quarkuscli.utils.TemplateDefinitionUtils
import com.primordium.core.coreutils.functional.ErrorResponse
import com.primordium.core.coreutils.functional.SuccessResponse
import com.primordium.core.domain.templates.TemplateDefinition
import com.primordium.core.domain.templates.TemplateDefinitionField
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.nio.file.Paths

open class TemplateDefinitionUtilsTest {

    @Test
    fun testHappyPath() {
        //Given
        val templateDefinitionPath = Thread.currentThread().contextClassLoader.getResourceAsStream("META-INF/default-files/default-primordium-template-definition.yaml")!!

        //When
        val result = TemplateDefinitionUtils.toTemplateDefinition(String(templateDefinitionPath.readAllBytes()))

        //Then
        if (result is ErrorResponse) {
            fail("No error response expected: ${result.errorMessages.joinToString(";")}")
        } else {
            val templateDefinitionResult = (result as SuccessResponse<TemplateDefinition>).data
            assertThat(templateDefinitionResult).isEqualTo(
                TemplateDefinition(
                    name = "Primordium Template",
                    description = """
                    A version of the template to configure and validate for Primordium's use (i.e., generation of deployment files).
                """.trimIndent(),
                    version = "0.0.1",
                    fields = listOfNotNull(
                        TemplateDefinitionField(
                            name = "application-checks",
                            description = "Checks that need to be implemented to define the overall health and responsiveness of the deployed application.",
                            required = true,
                            subFields = listOfNotNull(
                                TemplateDefinitionField(
                                    name = "heart-beat-check",
                                    description = "Checks that the application started.",
                                    required = true
                                ),
                                TemplateDefinitionField(
                                    name = "conscious-check",
                                    description = "Checks that the application is conscious (i.e., responding in a timely manner).",
                                    required = true
                                )
                            )
                        )
                    )
                )
            )
        }
    }
}