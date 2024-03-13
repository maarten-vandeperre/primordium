package com.primordium.configuration.quarkuscli

import com.primordium.configuration.quarkuscli.routing.Dispatcher
import io.quarkus.runtime.QuarkusApplication
import io.quarkus.runtime.annotations.QuarkusMain

@QuarkusMain
class CliApplication(
    private val dispatcher: Dispatcher
) : QuarkusApplication {

    override fun run(vararg args: String): Int {
        println(dispatcher.dispatch(*args.filter { it.isNotBlank() }.toTypedArray()))
        return 0
    }
}