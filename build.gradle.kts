@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform")
    jacoco
}

group = "com.handtruth"
version = "0.1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js()
    mingwX64()
    mingwX86()
    linuxX64()
    linuxArm32Hfp()
    linuxArm64()
    wasm32()
    sourceSets {
        all {
            dependencies {
                implementation(project(":platform"))
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit5"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

jacoco {
    toolVersion = "0.8.5"
    reportsDir = file("${buildDir}/jacoco-reports")
}

tasks {
    val testCoverageReport by creating(JacocoReport::class) {
        dependsOn("jvmTest")
        group = "Reporting"
        description = "Generate Jacoco coverage reports."
        val coverageSourceDirs = arrayOf(
                "commonMain/src",
                "jvmMain/src"
        )
        val classFiles = file("${buildDir}/classes/kotlin/jvm/")
                .walkBottomUp()
                .toSet()
        classDirectories.setFrom(classFiles)
        sourceDirectories.setFrom(files(coverageSourceDirs))
        additionalSourceDirs.setFrom(files(coverageSourceDirs))

        executionData.setFrom(files("${buildDir}/jacoco/jvmTest.exec"))
        reports {
            xml.isEnabled = true
            csv.isEnabled = false
            html.isEnabled = true
            html.destination = file("${buildDir}/jacoco-reports/html")
        }
    }
}
