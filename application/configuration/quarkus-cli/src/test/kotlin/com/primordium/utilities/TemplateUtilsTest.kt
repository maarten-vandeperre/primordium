package com.primordium.utilities

import com.fasterxml.jackson.databind.ObjectMapper
import com.primordium.configuration.quarkuscli.utils.TemplateUtils
import com.primordium.core.coreutils.functional.ErrorResponse
import com.primordium.core.coreutils.functional.SuccessResponse
import com.primordium.core.domain.templates.Template
import com.primordium.core.domain.templates.TemplateField
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

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
            assertThat(ObjectMapper().writeValueAsString(templateResult)).isEqualTo(
                """
                    {
                       "name":"Primordium Application",
                       "version":"0.0.1",
                       "fields":[
                          {
                             "name":"metadata",
                             "value":null,
                             "subFields":[
                                {
                                   "name":"name",
                                   "value":"primordium-application",
                                   "subFields":[
                                      
                                   ]
                                },
                                {
                                   "name":"namespace",
                                   "value":"primordium-project",
                                   "subFields":[
                                      
                                   ]
                                },
                                {
                                   "name":"port",
                                   "value":"8080",
                                   "subFields":[
                                      
                                   ]
                                },
                                {
                                   "name":"instance-count",
                                   "value":"2",
                                   "subFields":[
                                      
                                   ]
                                },
                                {
                                   "name":"image",
                                   "value":"image-registry.openshift-image-registry.svc:5000/webinar/person-service@sha256:a89c47d43f1f7aa527bffce3bb5c92d63be2211f5ec93b47cd5cb0f456c7ac05",
                                   "subFields":[
                                      
                                   ]
                                }
                             ]
                          },
                          {
                             "name":"application-checks",
                             "value":null,
                             "subFields":[
                                {
                                   "name":"heart-beat-check",
                                   "value":"/probes/liveness",
                                   "subFields":[
                                      
                                   ]
                                },
                                {
                                   "name":"conscious-check",
                                   "value":"/probes/readyness",
                                   "subFields":[
                                      
                                   ]
                                },
                                {
                                   "name":"birth-check",
                                   "value":"/probes/startup",
                                   "subFields":[
                                      
                                   ]
                                }
                             ]
                          }
                       ]
                    }
                """.trimIndent().replace(Regex("[\t\r\n]+"), "").replace(Regex(" {2,}"), "")
            )
        }
    }
}