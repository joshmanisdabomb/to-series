plugins {
    id("multiloader-loader")
    alias(libs.plugins.loom)
}

val modId: String by project

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.parchmentMC.get()}:${libs.versions.parchment.get()}@zip")
    })
    modImplementation(libs.fabricLoader)
    modImplementation(libs.fabricApi)

    modImplementation(libs.flk)

    api(libs.commonmark)
    include(libs.commonmark)
    api(libs.commonmark.yaml)
    include(libs.commonmark.yaml)
    api(libs.modeshape.common)
    include(libs.modeshape.common)
}

configurations.annotationProcessor {
    resolutionStrategy {
        force("org.spongepowered:mixin:0.8.5:processor")
    }
}

loom {
    officialMojangMappings()

    val aw = project(":to_base:common").file("src/main/resources/${modId}.accesswidener")
    if (aw.exists()) {
        accessWidenerPath.set(aw)
    }
    mixin {
        defaultRefmapName.set("${modId}.refmap.json")
    }
    runs {
        named("client") {
            client()
            configName = "${project.extra["modName"]}: Fabric Client"
            appendProjectPathToConfigName = false
            ideConfigGenerated(true)
            runDir("runs/client")
            property("fabric-tag-conventions-v2.missingTagTranslationWarning", "VERBOSE")
            property("mixin.debug.export.decompile", "true")
            property("mixin.debug.export", "true")
        }
        named("server") {
            server()
            configName = "${project.extra["modName"]}: Fabric Server"
            appendProjectPathToConfigName = false
            ideConfigGenerated(true)
            runDir("runs/server")
            property("fabric-tag-conventions-v2.missingTagTranslationWarning", "VERBOSE")
            property("mixin.debug.export.decompile", "true")
            property("mixin.debug.export", "true")
        }
    }
}
