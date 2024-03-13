plugins {
    `java-platform`
}

javaPlatform {
    allowDependencies()
}
dependencies {
    api(platform("io.quarkus:quarkus-bom:${properties.get("quarkusPlatformVersion")}"))
    api(platform("org.assertj:assertj-core:3.25.3"))
}




