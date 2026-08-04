package net.jidb.to.base.fabric.platform

import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext
import net.jidb.to.base.api.level.biome.BiomeMod
import net.jidb.to.base.api.platform.BiomePlatformModule

/**
 * [BiomePlatformModule] implementation for Fabric.
 *
 * @since 0.3.0
 */
object BiomeFabricPlatformModule : BiomePlatformModule() {

    override fun registerBiomeMod(mod: BiomeMod) {
        for (feature in mod.features) {
            val predicate: (context: BiomeSelectionContext) -> Boolean = { context -> context.biomeKey in feature.biomes || feature.tags.any { context.hasTag(it) } }
            for (pfeature in feature.features) {
                BiomeModifications.addFeature(
                    predicate,
                    feature.step,
                    pfeature
                )
            }
        }
    }

}
