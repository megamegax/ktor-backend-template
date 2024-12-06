plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinx.serialization)
}

group = "hu.hanprog.backend"
version = ""

application {
    mainClass.set("hu.hanprog.backend.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.logging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.serialization)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.logback)
    implementation(libs.logstash.logback.encoder)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.slf4j)
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.kotysa.r2dbc)
    implementation(libs.r2dbc.mysql)
    implementation(libs.r2dbc.pool)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.dotenv)
    implementation(libs.koin)
    implementation(libs.koin.logger)

    testImplementation(libs.ktor.server.test)
    testImplementation(libs.koin.test)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.property)
    testImplementation(libs.junit.platform.engine)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.testcontainers.mysql)
    testImplementation(kotlin("test"))

    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.115.Final:osx-aarch_64")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}