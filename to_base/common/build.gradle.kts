plugins {
    id("multiloader-common")
    alias(libs.plugins.moddev)
}

neoForge {
    neoFormVersion = libs.versions.neoForm.get()
    // Automatically enable AccessTransformers if the file exists
    val at = file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
    }
    // Puts the deobfuscated Minecraft jar and its libraries on the test classpath as well as the main
    // one, so that a plain JUnit test can use the game's own types. This is not neoForge.unitTest,
    // which boots FML and is only available to a project that names a full Neoforge version.
    addModdingDependenciesTo(sourceSets.test.get())
}

dependencies {
    compileOnly(libs.mixin)
    // fabric and neoforge both bundle mixinextras, so it is safe to use it in common
    compileOnly(libs.mixinExtras.common)

    api(libs.commonmark)
    api(libs.commonmark.yaml)
    api(libs.modeshape.common)
}

configurations {
    create("commonJava") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    create("commonKotlin") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    create("commonResources") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}

artifacts {
    add("commonJava", sourceSets.main.get().java.sourceDirectories.singleFile)
    add("commonKotlin", sourceSets.main.get().kotlin.sourceDirectories.filter { !it.name.endsWith("java") }.singleFile)
    add("commonResources", sourceSets.main.get().resources.sourceDirectories.singleFile)
}
