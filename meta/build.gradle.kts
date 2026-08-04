// Custom Detekt rules for conventions that Detekt has no built-in check for. Consumed by the mod
// projects through detektPlugins in multiloader-common.gradle, which is why this is a real
// subproject rather than something inside buildSrc: Detekt needs the rules on the analysis
// classpath at execution time, not on the buildscript classpath.
plugins {
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    // Provided by Detekt itself at analysis time.
    compileOnly("dev.detekt:detekt-api:${libs.versions.detekt.get()}")
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}
