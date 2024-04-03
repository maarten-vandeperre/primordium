package com.primordium.utilities

import com.primordium.configuration.quarkuscli.utils.TemplateUtils
import com.primordium.core.coreutils.functional.ErrorResponse
import com.primordium.core.coreutils.functional.SuccessResponse
import com.primordium.core.domain.templates.Template
import com.primordium.core.domain.templates.TemplateField
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.nio.file.Paths

//TODO test and on template definition test if file exists
open class TemplateUtilsTest {

    @Test
    fun testHappyPath() {
        //Given
        val templateContent = Thread.currentThread().contextClassLoader.getResourceAsStream("META-INF/default-files/default-primordium-template.yaml")!!

        //When
        val result = TemplateUtils.toTemplate(String(templateContent.readAllBytes()))

        //Then
        if (result is ErrorResponse) {
            fail("No error response expected: ${result.errorMessages.joinToString(";")}")
        } else {
            val templateResult = (result as SuccessResponse<Template>).data
            assertThat(templateResult).isEqualTo(
                Template(
                    name = "Primordium Template",
                    version = "0.0.1",
                    fields = listOf(
                        TemplateField(
                            name = "application-checks",
                            subFields = listOf(
                                TemplateField(
                                    name = "heart-beat-check",
                                    value = "a heart beat check implementation"
                                ),
                                TemplateField(
                                    name = "conscious-check",
                                    value = "a consciousness check implementation"
                                )
                            )
                        )
                    )
                )
            )
        }
    }
}