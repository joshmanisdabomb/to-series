plugins {
    id("multiloader-loader")
    alias(libs.plugins.loom)
}

val modId: String by project

dependencies {
    minecraft(libs.minecraft)

    implementation(libs.fabricLoader)
    implementation(libs.fabricApi)

    implementation(libs.flk)

    compileOnly(libs.techReborn)

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
            displayName.set("${project.extra["modName"]}: Fabric Client")
            appendProjectPathToDisplayName.set(false)
            generateRunConfig.set(true)
            runDirectory.set(project.file("runs/client"))
            systemProperties.put("fabric-tag-conventions-v2.missingTagTranslationWarning", "VERBOSE")
            systemProperties.put("mixin.debug.export.decompile", "true")
            systemProperties.put("mixin.debug.export", "true")
        }
        named("server") {
            server()
            displayName.set("${project.extra["modName"]}: Fabric Server")
            appendProjectPathToDisplayName.set(false)
            generateRunConfig.set(true)
            runDirectory.set(project.file("runs/server"))
            systemProperties.put("fabric-tag-conventions-v2.missingTagTranslationWarning", "VERBOSE")
            systemProperties.put("mixin.debug.export.decompile", "true")
            systemProperties.put("mixin.debug.export", "true")
        }
    }
}
