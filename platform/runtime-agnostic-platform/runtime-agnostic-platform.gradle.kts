plugins {
    `java-platform`
}

javaPlatform {
    allowDependencies()
}
dependencies {
    api(platform("org.assertj:assertj-core:3.25.3"))
    api(platform("junit:junit:4.13.2"))
}




