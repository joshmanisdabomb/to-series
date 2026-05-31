import org.gradle.internal.extensions.stdlib.capitalized

plugins {
    id("multiloader-loader")
    alias(libs.plugins.moddev)
}

val modId: String by project

neoForge {
    version = libs.versions.neoforge.get()
    // Automatically enable neoforge AccessTransformers if the file exists
    val at = project(":to_base:common").file("src/main/resources/META-INF/accesstransformer.cfg")
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
            systemProperty("mixin.debug.export.decompile", "true")
            systemProperty("mixin.debug.export", "true")
            ideName = "${project.extra["modName"]}: NeoForge ${name.capitalized()}" // Unify the run config names with fabric
        }
        register("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }
        register("server") {
            server()
            programArgument("--nogui")
        }
        register("data") {
            clientData()
            systemProperty("neoforge.enabledGameTestNamespaces", modId)

            // Specify the modid for data generation, where to output the resulting resource, and where to look for existing resources.
            programArguments.addAll(
                "--mod", modId,
                "--all",
                "--output", file("src/main/generated/").absolutePath,
                "--existing", file("../common/src/main/resources/").absolutePath
            )
            systemProperty("net.jidb.to.base.data.safe", file("../common/src/main/resources/").absolutePath)
        }
        register("test") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }
    }
    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}

dependencies {
    implementation(libs.kff)

    api(libs.commonmark)
    jarJar(libs.commonmark) {
    }
    api(libs.commonmark.yaml)
    jarJar(libs.commonmark.yaml) {
    }
    api(libs.modeshape.common)
    jarJar(libs.modeshape.common) {
    }
}
