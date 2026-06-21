package net.jidb.to.base.api.platform

import net.jidb.to.base.api.level.biome.BiomeMod

abstract class BiomePlatformModule {

    abstract fun registerBiomeMod(mod: BiomeMod)

}
