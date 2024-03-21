plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.allopen") version "1.9.22"
}

repositories {
    mavenCentral()
    mavenLocal()
}

val applicationVersion: String = File(".version").readBytes().toString().trim()

group = "com.primordium"
version = applicationVersion

subprojects.filter { !(it.name == "platform" || it.parent?.name == "platform") }.forEach {
    println("configure ${it.name}")
    it.group = "com.primordium"
    it.version = applicationVersion

    it.repositories {
        mavenCentral()
        mavenLocal()
    }

    it.apply(plugin = "idea")
    it.apply(plugin = "org.jetbrains.kotlin.jvm")
    it.apply(plugin = "org.jetbrains.kotlin.plugin.allopen")

    it.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21

        sourceSets.getByName("main") {
            java.srcDir("src/main/kotlin")
        }
        sourceSets.getByName("test") {
            java.srcDir("src/test/kotlin")
        }
    }

    it.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_21.toString()
            javaParameters = true
        }
    }

    it.dependencies {
        implementation(platform(project(":platform:runtime-agnostic-platform")))
        testImplementation("junit:junit")
        testImplementation("org.assertj:assertj-core")
    }
}
