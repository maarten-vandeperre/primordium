package com.primordium.utilities

import com.fasterxml.jackson.databind.ObjectMapper
import com.primordium.configuration.quarkuscli.utils.TemplateDefinitionUtils
import com.primordium.core.coreutils.functional.ErrorResponse
import com.primordium.core.coreutils.functional.SuccessResponse
import com.primordium.core.domain.templates.TemplateDefinition
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

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
            assertThat(ObjectMapper().writeValueAsString(templateDefinitionResult)).isEqualTo(
                """
                    {
                       "name":"Primordium Template",
                       "description":"A version of the template to configure and validate for Primordium's use (i.e., generation of deployment files).",
                       "version":"0.0.1",
                       "fields":[
                          {
                             "name":"metadata",
                             "description":"Metadata configurations in order to get the application deployed and to make the application accessible.",
                             "required":true,
                             "subFields":[
                                {
                                   "name":"name",
                                   "description":"The name of the application that will get deployed.",
                                   "required":true,
                                   "subFields":[
                                      
                                   ]
                                },
                                {
                                   "name":"name",
                                   "description":"The namespace in which the application that will get deployed.",
                                   "required":true,
                                   "subFields":[
                                      
                                   ]
                                },
                                {
                                   "name":"port",
                                   "description":"The port on which the application will be accessible.",
                                   "required":true,
                                   "subFields":[
                                      
                                   ]
                                },
                                {
                                   "name":"instance-count",
                                   "description":"The amount of instances of the specified application that need to run.",
                                   "required":true,
                                   "subFields":[
                                      
                                   ]
                                },
                                {
                                   "name":"image",
                                   "description":"The reference of the podman/docker/... image that will be deployed.",
                                   "required":true,
                                   "subFields":[
                                      
                                   ]
                                }
                             ]
                          },
                          {
                             "name":"application-checks",
                             "description":"Checks that need to be implemented to define the overall health and responsiveness of the deployed application.",
                             "required":true,
                             "subFields":[
                                {
                                   "name":"heart-beat-check",
                                   "description":"Checks that the application started and is active (i.e., no unrecoverable application failure like a dead lock occurred).",
                                   "required":true,
                                   "subFields":[
                                      
                                   ]
                                },
                                {
                                   "name":"conscious-check",
                                   "description":"Checks that the application is conscious (i.e., responding in a timely manner).",
                                   "required":true,
                                   "subFields":[
                                      
                                   ]
                                },
                                {
                                   "name":"birth-check",
                                   "description":"Checks that the application is started (i.e., has a heart beat, but this check can have a different polling/checking interval).",
                                   "required":true,
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