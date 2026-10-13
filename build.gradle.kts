import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    kotlin("jvm") version "2.4.20"
    id("com.google.devtools.ksp") version "2.3.12"
}

group = "com.github.marcoral.versatia"
version = "0.0.1"

val apiVersion = "0.0.1"

repositories {
    // The Versatia artifacts are consumed from the local Maven repository (`./gradlew publishToMavenLocal` in each checkout)
    mavenLocal()
    mavenCentral()
    // Needed to resolve the Paper API that VersatiaAPI exports
    maven("https://repo.papermc.io/repository/maven-public/")
}

// The starter exports VersatiaAPI (with the Paper API and Kotlin) and the module APIs; nothing is bundled,
// on the server these classes come from VersatiaCore and the module plugins (see plugin.yml)
dependencies {
    implementation("com.github.marcoral.versatia:VersatiaStarter:$apiVersion")
    ksp("com.github.marcoral.versatia:VersatiaAPI-ksp:$apiVersion")
}

// Paper 1.21.4 requires Java 21
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

// Expands ${version} in plugin.yml
tasks.processResources {
    val props = mapOf("version" to project.version)
    inputs.properties(props)
    filesMatching("plugin.yml") {
        expand(props)
    }
}

// Machine-specific settings; see README.md
val localProperties: Provider<Properties> = providers
    .fileContents(layout.projectDirectory.file("local.properties"))
    .asText
    .map { text -> Properties().apply { load(text.reader()) } }

val pluginsDir: Provider<String> = providers.gradleProperty("pluginsDir")
    .orElse(localProperties.map { it.getProperty("pluginsDir") })

tasks.register<Copy>("deployPlugin") {
    group = "versatia"
    description = "Copies the plugin JAR to the server plugins/ directory (pluginsDir from local.properties)."
    // Local copy: the onlyIf lambda must not capture script fields (configuration cache)
    val targetDir = pluginsDir
    onlyIf("pluginsDir is not set - see local.properties.example") { targetDir.isPresent }
    from(tasks.jar)
    into(targetDir.orElse(layout.buildDirectory.dir("deploy").map { it.asFile.path }))
}

tasks.build {
    finalizedBy(tasks.named("deployPlugin"))
}
