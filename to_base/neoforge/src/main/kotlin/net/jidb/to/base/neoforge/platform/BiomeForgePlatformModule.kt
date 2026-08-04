package net.jidb.to.base.neoforge.platform

import net.jidb.to.base.api.level.biome.BiomeMod
import net.jidb.to.base.api.platform.BiomePlatformModule

/**
 * [BiomePlatformModule] implementation for Neoforge, which does nothing, because a biome modifier is a data pack file on this loader rather than something registered in code.
 *
 * @since 0.3.0
 */
object BiomeForgePlatformModule : BiomePlatformModule() {

    override fun registerBiomeMod(mod: BiomeMod) = Unit

}
