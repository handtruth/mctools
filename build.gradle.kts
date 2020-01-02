import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    jacoco
}

group = "com.handtruth.mc"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    val jacksonVersion: String by project

    implementation(project(":paket-kotlin"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(group = "com.fasterxml.jackson.core", name = "jackson-core", version = jacksonVersion)
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = jacksonVersion)

    testImplementation("org.junit.jupiter:junit-jupiter:5.5.1")
    testImplementation(kotlin("test"))
}

jacoco {
    toolVersion = "0.8.4"
    reportsDir = file("$buildDir/customJacocoReportDir")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
    jacocoTestReport {
        reports {
            xml.isEnabled = false
            csv.isEnabled = false
            html.destination = file("$buildDir/jacocoHtml")
        }
    }
}
