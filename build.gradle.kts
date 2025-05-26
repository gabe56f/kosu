plugins {
    kotlin("jvm") version libs.versions.kotlin.version
    kotlin("plugin.serialization") version libs.versions.kotlin.version

    alias(libs.plugins.dokka)
}

group = "hu.gabe"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(libs.okhttp)
    implementation(libs.serialization.json)
    implementation(libs.coroutines)
    implementation(libs.datetime)
    implementation(libs.kache)

    testImplementation(kotlin("test"))
}

tasks.test {
    systemProperty("kosu_client_id", project.findProperty("client.id")!!)
    systemProperty("kosu_client_secret", project.findProperty("client.secret")!!)
    systemProperty("kosu_client_api_key", project.findProperty("client.apiKey")!!)
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(22)
}