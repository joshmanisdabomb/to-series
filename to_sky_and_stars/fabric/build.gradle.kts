plugins {
    id("multiloader-loader")
    alias(libs.plugins.loom)
}

val modId: String by project

val subprojectInclude = extra["subprojectInclude"] as groovy.lang.Closure<*>

dependencies {
    minecraft(libs.minecraft)

    implementation(libs.fabricLoader)
    implementation(libs.fabricApi)

    implementation(libs.flk)
}

subprojectInclude.call(":to_base")

configurations.annotationProcessor {
    resolutionStrategy {
        force("org.spongepowered:mixin:0.8.5:processor")
    }
}

loom {
    val aw = project(":to_sky_and_stars:common").file("src/main/resources/${modId}.accesswidener")
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
