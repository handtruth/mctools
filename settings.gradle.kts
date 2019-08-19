import java.io.File

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("org.jetbrains.kotlin")) {
                useVersion(gradle.rootProject.extra["kotlin.version"] as String)
            }
        }
    }
}

rootProject.name = "mcproto"
include(":paket-kotlin")
project(":paket-kotlin").projectDir = File("modules/paket-kotlin")
