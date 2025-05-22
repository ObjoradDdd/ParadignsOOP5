plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.1.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")

    testImplementation("io.mockk:mockk:1.13.4")

    testImplementation("org.assertj:assertj-core:3.24.2")

    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
}

tasks.test {
    useJUnitPlatform()
}