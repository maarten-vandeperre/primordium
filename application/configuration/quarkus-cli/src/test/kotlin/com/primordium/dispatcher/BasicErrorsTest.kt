package com.primordium.dispatcher

import io.quarkus.test.junit.main.Launch
import io.quarkus.test.junit.main.LaunchResult
import io.quarkus.test.junit.main.QuarkusMainTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@QuarkusMainTest
class BasicErrorsTest {

    @Test
    @Launch("test")
    fun unknownCommand(result: LaunchResult){
        assertThat(result.output).isEqualTo("""
            Command execution failed:
                Unknown command for 'test'. Try running --help.
        """.trimIndent())
    }

    @Test
    @Launch("")
    fun noArgs(result: LaunchResult){
        assertThat(result.output).isEqualTo("""
            Command execution failed:
                No parameters are given, try ' --help' to get more information
        """.trimIndent())
    }
}