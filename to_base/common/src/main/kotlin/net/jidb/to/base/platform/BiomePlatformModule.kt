package net.jidb.to.base.platform

import net.jidb.to.base.level.biome.BiomeMod

abstract class BiomePlatformModule {

    abstract fun registerBiomeMod(mod: BiomeMod)

}
