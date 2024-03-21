package com.primordium.core.usecases.templates

import com.primordium.core.coreutils.functional.ErrorResponse
import com.primordium.core.coreutils.functional.SuccessResponse
import com.primordium.core.domain.templates.FieldName
import com.primordium.core.domain.templates.FieldValue
import com.primordium.core.domain.templates.Template
import com.primordium.core.domain.templates.TemplateField
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DefaultProcessTemplateUseCaseTest {

    @Test
    fun testPropertyRetrieval() {
        //Given
        val template = DEFAULT_TEMPLATE

        //When
        val result: Map<FieldName, FieldValue> = (DefaultProcessTemplateUseCase.listPossibleProperties(template) as SuccessResponse).data

        //Then
        val properties = result.toString()
        assertThat(properties).isEqualTo(
            """
                {field1=value1, field2.field2a=value2a, field2.field2b=value2b}
            """.trimIndent()
        )
    }

    @Test
    fun testFillPlaceholder() {
        //Given
        val template = DEFAULT_TEMPLATE
        val placeholder = """
            maarten is testing '<<field1>>' and it is returning:
                <<field2.field2a>>
                <<field2.field2a>>
                <<field2.field2b>>
        """.trimIndent()

        //When
        val response = DefaultProcessTemplateUseCase.fillPlaceholder(placeholder, template)
        if(response is ErrorResponse){
            println(response.errorMessages.joinToString("\n"))
        }
        val filledPlaceholder: String = (response as SuccessResponse).data

        //Then
        assertThat(filledPlaceholder).isEqualTo(
            """
                maarten is testing 'value1' and it is returning:
                    value2a
                    value2a
                    value2b
            """.trimIndent()
        )
    }

    companion object {
        private val DEFAULT_TEMPLATE = Template(
            name = "test",
            version = "0.0.1",
            fields = listOf(
                TemplateField(
                    name = "field1",
                    value = "value1"
                ),
                TemplateField(
                    name = "field2",
                    value = "value2",
                    subFields = listOf(
                        TemplateField(
                            name = "field2a",
                            value = "value2a"
                        ),
                        TemplateField(
                            name = "field2b",
                            value = "value2b"
                        )
                    )
                )
            )
        )
    }
}