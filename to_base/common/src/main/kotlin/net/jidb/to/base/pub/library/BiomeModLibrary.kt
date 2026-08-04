package net.jidb.to.base.pub.library

import net.jidb.to.base.api.level.biome.BiomeMod
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.service.Services

/**
 * [SimpleLibrary] implementation that registers [BiomeMod] content with the current platform, and provides access to that content in one place.
 * A biome modification is not registry content, so each entry is handed to the platform as it is built rather than written into a [net.minecraft.core.Registry].
 *
 * @param modid The mod ID associated with the library.
 * @since 0.3.0
 */
open class BiomeModLibrary(modid: String) : SimpleLibrary<BiomeMod>(modid) {

    override fun afterBuild(entry: Library<BiomeMod, BiomeMod>.LibraryEntry<out BiomeMod, out BiomeMod>) {
        Services.platform.biomes.registerBiomeMod(entry.value)
    }

}
