import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.gladed.androidgitversion")
    kotlin("jvm")
    jacoco
    `maven-publish`
}

androidGitVersion {
    prefix = "v"
}

group = "com.handtruth.mc"
version = androidGitVersion.name()

repositories {
    mavenCentral()
    maven("https://mvn.handtruth.com")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
        }
    }
}

dependencies {
    val platformVersion: String by project
    implementation(platform("com.handtruth.internal:platform:$platformVersion"))

    implementation("com.handtruth.mc:paket-kotlin:2.0.0")

    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}

jacoco {
    toolVersion = "0.8.5"
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
