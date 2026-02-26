import org.gradle.internal.extensions.stdlib.capitalized

plugins {
    id("multiloader-loader")
    alias(libs.plugins.moddev)
}

val modId: String by project

neoForge {
    version = libs.versions.neoforge.get()
    // Automatically enable neoforge AccessTransformers if the file exists
    val at = project(":to_sky_and_stars:common").file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
    }
    parchment {
        minecraftVersion = libs.versions.parchmentMC
        mappingsVersion = libs.versions.parchment
    }
    runs {
        configureEach {
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
            ideName = "NeoForge ${name.capitalized()} (${project.path})" // Unify the run config names with fabric
        }
        register("client") {
            client()
        }
        register("data") {
            clientData()
        }
        register("server") {
            server()
        }
    }
    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main.get().resources { srcDir("src/generated/resources") }

dependencies {
    compileOnly(project(":to_base:neoforge")) {
        capabilities {
            requireCapability("net.jidb.to.base:to_base-neoforge")
        }
    }
    runtimeOnly(project(":to_base:neoforge")) {
        capabilities {
            requireCapability("net.jidb.to.base:to_base-neoforge")
        }
    }
    compileOnly(project(":to_base:common")) {
        capabilities {
            requireCapability("net.jidb.to.base:to_base-common")
        }
    }
    runtimeOnly(project(":to_base:common")) {
        capabilities {
            requireCapability("net.jidb.to.base:to_base-common")
        }
    }

    implementation(libs.kff)
}