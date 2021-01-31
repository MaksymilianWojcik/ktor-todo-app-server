import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar


val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project
val bcrypt_version: String by project
val exposed_version: String by project
val hikaricp_version: String by project
val postgres_version: String by project
//val h2_version: String by project

plugins {
    application
    kotlin("jvm") version "1.4.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.4.10"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "com.mwcode.server"
version = "1.0.0"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("org.koin:koin-core:$koin_version")
    implementation("org.koin:koin-ktor:$koin_version")
    implementation("org.mindrot:jbcrypt:$bcrypt_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jodatime:$exposed_version")
    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("org.postgresql:postgresql:$postgres_version")
//    implementation("com.h2database:h2:$h2_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

tasks {
    withType<ShadowJar> {
        archiveBaseName.set("todo-app-server")
        archiveVersion.set("1.0.0")
    }
}