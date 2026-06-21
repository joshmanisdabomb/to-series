package net.jidb.to.base.pub.library

import net.jidb.to.base.api.level.biome.BiomeMod
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.service.Services

open class BiomeModLibrary(modid: String) : SimpleLibrary<BiomeMod>(modid) {

    override fun afterBuild(entry: Library<BiomeMod, BiomeMod>.LibraryEntry<out BiomeMod, out BiomeMod>) {
        Services.platform.biomes.registerBiomeMod(entry.value)
    }

}
