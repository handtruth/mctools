import java.io.File

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
    }
}

rootProject.name = "mcproto"
include(
    ":platform",
    ":paket-kotlin"
)
project(":paket-kotlin").projectDir = File("modules/paket-kotlin")
