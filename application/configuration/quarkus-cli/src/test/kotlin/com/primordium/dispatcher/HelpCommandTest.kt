package com.primordium.dispatcher

import io.quarkus.test.junit.main.Launch
import io.quarkus.test.junit.main.LaunchResult
import io.quarkus.test.junit.main.QuarkusMainTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@QuarkusMainTest
open class HelpCommandTest {
    @Test
    @Launch("--help")
    fun basicHelpCommand(result: LaunchResult) {
        assertThat(result.output).isEqualTo(
            """
            Primordium is a template engine to ....
  
            Basic commands:
                default-template-definition        Create a new default template definition.
        """.trimIndent()
        )
    }
}