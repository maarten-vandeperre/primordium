pluginManagement {
    val quarkusPluginVersion: String by settings
    val quarkusPluginId: String by settings
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
    plugins {
        id(quarkusPluginId) version quarkusPluginVersion
    }
}
rootProject.name = "primordium"

include(":platform:quarkus-platform")
include(":platform:runtime-agnostic-platform")

include(":application:configuration:quarkus-cli")

include(":application:core:core-utils")
include(":application:core:domain")
include(":application:core:usecases")

rootProject.children
    .flatMap { child -> if (child.children.isEmpty()) listOf(child) else child.children }
    .flatMap { child -> if (child.children.isEmpty()) listOf(child) else child.children }
    .flatMap { child -> if (child.children.isEmpty()) listOf(child) else child.children }
    .flatMap { child -> if (child.children.isEmpty()) listOf(child) else child.children }
    .forEach { subproject ->
        println("configure: " + subproject.name + ".gradle.kts")
        subproject.buildFileName = subproject.name + ".gradle.kts"
    }