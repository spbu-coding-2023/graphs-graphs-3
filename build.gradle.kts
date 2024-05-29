import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("jacoco")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    flatDir {
        dirs("libs")
    }
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation(":louvain-1.0-SNAPSHOT")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "graphs-3"
            packageVersion = "1.0.0"
        }
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport{
    dependsOn(tasks.test)
    reports{
        xml.required.set(true)
        html.required.set(true)
    }
}

jacoco {
    toolVersion = "0.8.12"
}