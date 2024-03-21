plugins {
    `java-platform`
}

javaPlatform {
    allowDependencies()
}
dependencies {
    api(platform("io.quarkus:quarkus-bom:${properties.get("quarkusPlatformVersion")}"))
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.17.0")
}




