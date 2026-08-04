plugins {
    id("groovy-gradle-plugin")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:${libs.versions.dokka.get()}")
    implementation("dev.detekt:detekt-gradle-plugin:${libs.versions.detekt.get()}")
}

repositories {
    mavenCentral()
}
