package com.primordium.functionality

import io.quarkus.test.junit.main.Launch
import io.quarkus.test.junit.main.LaunchResult
import io.quarkus.test.junit.main.QuarkusMainLauncher
import io.quarkus.test.junit.main.QuarkusMainTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.util.*
import kotlin.io.path.*

//TODO test with relative and absolute paths
//TODO test all commands with too many parameters (that don't exist)
@QuarkusMainTest
open class CreateDefaultTemplateDefinitionTest {
    @Test
    @Launch("default-template-definition")
    fun noDirectory(result: LaunchResult) {
        assertThat(result.output).isEqualTo(
            """
            Command execution failed:
                No directory parameter is given, try ' --help' to get more information
        """.trimIndent()
        )
    }

    @Test
    @Launch("default-template-definition --help")
    fun help(result: LaunchResult) {
        assertThat(result.output).isEqualTo(
            """
            Creates a new default template definition.
  
            Parameters (--parameter=...):
            *: required parameter
                directory*      Absolute or relative path to the directory where the file will be written.
                file-name       Override of the default file name (default = default-primordium-template.yaml).
        """.trimIndent()
        )
    }

    @OptIn(ExperimentalPathApi::class)
    @Test
    fun happyPath(launcher: QuarkusMainLauncher) {
        //Given
        val directory = Files.createTempDirectory("CreateDefaultTemplateDefinitionTest-${UUID.randomUUID().toString()}")

        try {
            //When
            val cmdResult: LaunchResult = launcher.launch("default-template-definition --directory=${directory.absolutePathString()} --stacktrace")
            val fileContent = String(Files.readAllBytes(Path("${directory.absolutePathString()}/default-primordium-template.yaml")))

            //Then
            assertThat(cmdResult.output).isEqualTo(
                """
                Generated the default template definition (${directory.absolutePathString()}/default-primordium-template.yaml).
                """.trimIndent()
            )
            //And
            assertThat(fileContent).isEqualTo(
                """
                    ---
                    name: "Primordium Template"
                    description: "A version of the template to configure and validate for Primordium's use (i.e., generation of deployment files)."
                    version: "0.0.1"
                    fields:
                    - name: "metadata"
                      description: "Metadata configurations in order to get the application deployed and to make the application accessible."
                      required: true
                      subFields:
                      - name: "name"
                        description: "The name of the application that will get deployed."
                        required: true
                        subFields: []
                      - name: "name"
                        description: "The namespace in which the application that will get deployed."
                        required: true
                        subFields: []
                      - name: "port"
                        description: "The port on which the application will be accessible."
                        required: true
                        subFields: []
                      - name: "instance-count"
                        description: "The amount of instances of the specified application that need to run."
                        required: true
                        subFields: []
                      - name: "image"
                        description: "The reference of the podman/docker/... image that will be deployed."
                        required: true
                        subFields: []
                    - name: "application-checks"
                      description: "Checks that need to be implemented to define the overall health and responsiveness of the deployed application."
                      required: true
                      subFields:
                      - name: "heart-beat-check"
                        description: "Checks that the application started and is active (i.e., no unrecoverable application failure like a dead lock occurred)."
                        required: true
                        subFields: []
                      - name: "conscious-check"
                        description: "Checks that the application is conscious (i.e., responding in a timely manner)."
                        required: true
                        subFields: []
                      - name: "birth-check"
                        description: "Checks that the application is started (i.e., has a heart beat, but this check can have a different polling/checking interval)."
                        required: true
                        subFields: []

                """.trimIndent()
            )
        } finally {
            directory.deleteRecursively()
        }
    }

    @Test
    fun happyPathWithFile(launcher: QuarkusMainLauncher) {
        //Given
        val directory = Files.createTempDirectory("CreateDefaultTemplateDefinitionTest-${UUID.randomUUID().toString()}")
        val fileName = "CreateDefaultTemplateDefinitionTest-${UUID.randomUUID().toString()}.yaml"


        try {
            //When
            val cmdResult: LaunchResult = launcher.launch("default-template-definition --directory=${directory.absolutePathString()} --file-name=${fileName} --stacktrace")
            val fileContent = String(Files.readAllBytes(Path("${directory.absolutePathString()}/$fileName")))

            //Then
            assertThat(cmdResult.output).isEqualTo(
                """
                Generated the default template definition (${directory.absolutePathString()}/$fileName).
                """.trimIndent()
            )
            //And
            assertThat(fileContent).isEqualTo(
                """
                    ---
                    name: "Primordium Template"
                    description: "A version of the template to configure and validate for Primordium's use (i.e., generation of deployment files)."
                    version: "0.0.1"
                    fields:
                    - name: "metadata"
                      description: "Metadata configurations in order to get the application deployed and to make the application accessible."
                      required: true
                      subFields:
                      - name: "name"
                        description: "The name of the application that will get deployed."
                        required: true
                        subFields: []
                      - name: "name"
                        description: "The namespace in which the application that will get deployed."
                        required: true
                        subFields: []
                      - name: "port"
                        description: "The port on which the application will be accessible."
                        required: true
                        subFields: []
                      - name: "instance-count"
                        description: "The amount of instances of the specified application that need to run."
                        required: true
                        subFields: []
                      - name: "image"
                        description: "The reference of the podman/docker/... image that will be deployed."
                        required: true
                        subFields: []
                    - name: "application-checks"
                      description: "Checks that need to be implemented to define the overall health and responsiveness of the deployed application."
                      required: true
                      subFields:
                      - name: "heart-beat-check"
                        description: "Checks that the application started and is active (i.e., no unrecoverable application failure like a dead lock occurred)."
                        required: true
                        subFields: []
                      - name: "conscious-check"
                        description: "Checks that the application is conscious (i.e., responding in a timely manner)."
                        required: true
                        subFields: []
                      - name: "birth-check"
                        description: "Checks that the application is started (i.e., has a heart beat, but this check can have a different polling/checking interval)."
                        required: true
                        subFields: []

                """.trimIndent()
            )
        } finally {
            Path("${directory.absolutePathString()}/$fileName").deleteIfExists()
            directory.deleteIfExists()
        }
    }
}