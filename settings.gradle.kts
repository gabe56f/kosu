plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    kotlin("jvm") version "2.2.20" apply false
}

rootProject.name = "kosu"
includeBuild("compiler-plugin")

include(":core")
