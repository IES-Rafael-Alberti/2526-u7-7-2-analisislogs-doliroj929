plugins {
    kotlin("jvm") version "2.3.0"
}

group = "org.iesra"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.github.ajalt.clikt:clikt-jvm:4.4.0")

}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}