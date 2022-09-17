import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
}

group = "com.vfom"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("space.kscience:kmath-complex:0.2.1")
    api("org.jetbrains.lets-plot:lets-plot-kotlin:4.0.0")
    api("org.jetbrains.lets-plot:lets-plot-image-export:2.4.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}