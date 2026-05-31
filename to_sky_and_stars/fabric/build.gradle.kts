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
