plugins {
    kotlin("jvm") version "2.2.0"
    application
}

group = "org.kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(23)

    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xcontext-parameters",
            "-Xcontext-sensitive-resolution",
            "-Xnested-typealiases",
            "-Xnested-type-aliases",
            "-Xwhen-guards",
            "-Xuse-site-annotations-defaulting",
            "-opt-in=kotlin.io.encoding.ExperimentalEncodingApi",
            "-opt-in=kotlin.text.ExperimentalStdlibApi"
        )

        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_23)
    }
}

application {
    mainClass.set("MainKt")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xdebug")
    }
}