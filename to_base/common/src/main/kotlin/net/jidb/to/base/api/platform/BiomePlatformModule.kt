package net.jidb.to.base.api.platform

import net.jidb.to.base.api.level.biome.BiomeMod

/**
 * A [Platform]-specific module that defines a cross-platform contract for handling biomes and other worldgen.
 * This module has code for registering a [BiomeMod] to make modifications to existing vanilla biomes.
 *
 * @since 0.3.0
 */
abstract class BiomePlatformModule {

    /**
     * Registers a [BiomeMod] to apply modifications, such as adding custom features, to existing vanilla biomes during world generation.
     *
     * @param mod The cross-platform [BiomeMod] instance, containing the desired modifications to apply to biomes.
     * @since 0.3.0
     */
    abstract fun registerBiomeMod(mod: BiomeMod)

}
