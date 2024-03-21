plugins {
    id("io.quarkus")
}

dependencies {
    implementation(platform(project(":platform:quarkus-platform")))

    implementation(project(":application:core:core-utils"))
    implementation(project(":application:core:domain"))
    implementation(project(":application:core:usecases"))

    implementation("io.quarkus:quarkus-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.quarkus:quarkus-arc")
    testImplementation("io.quarkus:quarkus-junit5")
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

allOpen {
    annotation("jakarta.enterprise.context.ApplicationScoped")
}