import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "com.ssau"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    api("space.kscience:kmath-complex:0.2.1")
    api("org.jetbrains.lets-plot:lets-plot-kotlin:4.0.0")
    api("org.jetbrains.lets-plot:lets-plot-image-export:2.4.0")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
