plugins {
    kotlin("jvm") version "2.2.20"
    `kotlin-dsl`

    id("com.google.devtools.ksp") version "2.2.20-2.0.3"
}

group = "hu.gabe"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.auto.service:auto-service-annotations:1.1.1")
    ksp("dev.zacsweers.autoservice:auto-service-ksp:1.2.0")

    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.2.20")
    compileOnly(kotlin("gradle-plugin"))
}

gradlePlugin {
    plugins {
        create("kosuPlugin") {
            id = "hu.gabe.kosu-plugin"
            implementationClass = "hu.gabe.kosu.gradle.CorePlugin"
        }
    }
}