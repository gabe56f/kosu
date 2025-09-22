plugins {
    kotlin("jvm") version libs.versions.kotlin.version
    kotlin("plugin.serialization") version libs.versions.kotlin.version
    `maven-publish`

    id("hu.gabe.kosu-plugin") version "1.0-SNAPSHOT"

    alias(libs.plugins.dokka)
    alias(libs.plugins.ksp)
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
    compileOnly("hu.gabe:compiler-plugin")

    testImplementation(kotlin("test"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["kotlin"])
        }
    }
}

tasks.test {
    systemProperty("kosu_client_id", project.findProperty("client.id")!!)
    systemProperty("kosu_client_secret", project.findProperty("client.secret")!!)
    systemProperty("kosu_client_api_key", project.findProperty("client.apiKey")!!)
    useJUnitPlatform()
}