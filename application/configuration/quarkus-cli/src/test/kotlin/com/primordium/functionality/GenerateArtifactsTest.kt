package com.primordium.functionality

import io.quarkus.test.junit.main.Launch
import io.quarkus.test.junit.main.LaunchResult
import io.quarkus.test.junit.main.QuarkusMainLauncher
import io.quarkus.test.junit.main.QuarkusMainTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.util.*
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.deleteRecursively

@QuarkusMainTest
open class GenerateArtifactsTest {

    @Test
    @Launch("generate-artifacts")
    fun noDirectory(result: LaunchResult) {
        Assertions.assertThat(result.output).isEqualTo(
            """
            Command execution failed:
                No directory parameter is given, try ' --help' to get more information
        """.trimIndent()
        )
    }

    @Test
    @Launch("generate-artifacts --help")
    fun help(result: LaunchResult) {
        Assertions.assertThat(result.output).isEqualTo(
            """
            Generates the artifacts like (docker-/podman-)compose files, Kubernetes deployment files, ....
  
            Parameters (--parameter=...):
            *: required parameter
                directory*      Absolute or relative path to the directory where the artifacts will be written.
        """.trimIndent()
        )
    }

    @OptIn(ExperimentalPathApi::class)
    @Test
    fun happyPath(launcher: QuarkusMainLauncher) {
        //Given
        val directory = Files.createTempDirectory("GenerateArtifactsTest-${UUID.randomUUID().toString()}")

        try {
            //When
            val cmdResult: LaunchResult = launcher.launch("generate-artifacts --directory=${directory.absolutePathString()} --stacktrace")

            //Then
            Assertions.assertThat(cmdResult.output).isEqualTo(
                """
                Generated the following files:
                    ${directory.absolutePathString()}/docker-podman/compose.yaml
                
                Couldn't generate the following files:
                    
                """.trimIndent()
            )
            //And
        } finally {
            directory.deleteRecursively()
        }
    }
}