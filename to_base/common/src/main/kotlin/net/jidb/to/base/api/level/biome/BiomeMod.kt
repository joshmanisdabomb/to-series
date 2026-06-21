package net.jidb.to.base.api.level.biome

import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.placement.PlacedFeature

class BiomeMod {

    protected val _features = mutableListOf<Feature>()
    val features: List<Feature> get() = _features

    fun addFeature(biomes: Iterable<ResourceKey<Biome>>, features: Iterable<ResourceKey<PlacedFeature>>, step: GenerationStep.Decoration): BiomeMod {
        _features.add(Feature(biomes.toSet(), emptySet(), features.toSet(), step))
        return this
    }
    @JvmName("addFeatureTag")
    fun addFeature(tags: Iterable<TagKey<Biome>>, features: Iterable<ResourceKey<PlacedFeature>>, step: GenerationStep.Decoration): BiomeMod {
        _features.add(Feature(emptySet(), tags.toSet(), features.toSet(), step))
        return this
    }

    fun addOverworldFeature(features: Iterable<ResourceKey<PlacedFeature>>, step: GenerationStep.Decoration) = addFeature(setOf(BiomeTags.IS_OVERWORLD), features, step)
    fun addNetherFeature(features: Iterable<ResourceKey<PlacedFeature>>, step: GenerationStep.Decoration) = addFeature(setOf(BiomeTags.IS_NETHER), features, step)
    fun addEndFeature(features: Iterable<ResourceKey<PlacedFeature>>, step: GenerationStep.Decoration) = addFeature(setOf(BiomeTags.IS_END), features, step)

    class Feature internal constructor(val biomes: Set<ResourceKey<Biome>>, val tags: Set<TagKey<Biome>>, val features: Set<ResourceKey<PlacedFeature>>, val step: GenerationStep.Decoration)

}
