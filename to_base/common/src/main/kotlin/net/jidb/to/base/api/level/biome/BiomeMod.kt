package net.jidb.to.base.api.level.biome

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.placement.PlacedFeature

/**
 * Class to chain together features to generate in any biome that resides in any dimension.
 * This is a cross-platform way to register the generation of features in both Neoforge and Fabric.
 *
 * @since 0.3.0
 */
class BiomeMod internal constructor(features: List<Feature>) {

    constructor() : this(emptyList())

    /**
     * Internally tracked list of feature entries to be added to any biome.
     *
     * @see Feature
     * @since 0.3.0
     */
    val features: List<Feature> field = features.toMutableList()

    /**
     * Adds features to the specified biomes for a given generation step.
     *
     * @param biomes A collection of biome resource keys to which the feature should be added.
     * @param features A collection of placed feature resource keys to include in the biomes.
     * @param step The decoration generation step during which the feature will be applied.
     * @return The current instance of [BiomeMod] for further chaining.
     * @since 0.3.0
     */
    fun addFeature(biomes: Iterable<ResourceKey<Biome>>, features: Iterable<ResourceKey<PlacedFeature>>, step: GenerationStep.Decoration): BiomeMod {
        this.features.add(Feature(biomes.toSet(), emptySet(), features.toSet(), step))
        return this
    }

    /**
     * Adds features to the specified biomes for a given generation step.
     *
     * @param tags A collection of biome tags to which the feature should be added.
     * @param features A collection of placed feature resource keys to include in the biomes.
     * @param step The decoration generation step during which the feature will be applied.
     * @return The current instance of [BiomeMod] for further chaining.
     * @since 0.3.0
     */
    @JvmName("addFeatureTag")
    fun addFeature(tags: Iterable<TagKey<Biome>>, features: Iterable<ResourceKey<PlacedFeature>>, step: GenerationStep.Decoration): BiomeMod {
        this.features.add(Feature(emptySet(), tags.toSet(), features.toSet(), step))
        return this
    }

    /**
     * Adds features to all overworld biomes for a given generation step.
     *
     * @param features A collection of placed feature resource keys to include in the overworld.
     * @param step The decoration generation step during which the feature will be applied.
     * @return The current instance of [BiomeMod] for further chaining.
     * @since 0.3.0
     */
    fun addOverworldFeature(features: Iterable<ResourceKey<PlacedFeature>>, step: GenerationStep.Decoration) = addFeature(setOf(BiomeTags.IS_OVERWORLD), features, step)

    /**
     * Adds features to all nether biomes for a given generation step.
     *
     * @param features A collection of placed feature resource keys to include in the Nether.
     * @param step The decoration generation step during which the feature will be applied.
     * @return The current instance of [BiomeMod] for further chaining.
     * @since 0.3.0
     */
    fun addNetherFeature(features: Iterable<ResourceKey<PlacedFeature>>, step: GenerationStep.Decoration) = addFeature(setOf(BiomeTags.IS_NETHER), features, step)

    /**
     * Adds features to all end biomes for a given generation step.
     *
     * @param features A collection of placed feature resource keys to include in The End.
     * @param step The decoration generation step during which the feature will be applied.
     * @return The current instance of [BiomeMod] for further chaining.
     * @since 0.3.0
     */
    fun addEndFeature(features: Iterable<ResourceKey<PlacedFeature>>, step: GenerationStep.Decoration) = addFeature(setOf(BiomeTags.IS_END), features, step)

    /**
     * Represents a set of features to be added to specific biomes during a particular generation step.
     *
     * @param biomes A collection of biome resource keys to which the feature should be added.
     * @param tags A collection of biome tags to which the feature should be added.
     * @param features A collection of placed feature resource keys to be included in the specified biomes.
     * @param step The decoration generation step during which the features will be applied.
     * @since 0.3.0
     */
    data class Feature(val biomes: Set<ResourceKey<Biome>>, val tags: Set<TagKey<Biome>>, val features: Set<ResourceKey<PlacedFeature>>, val step: GenerationStep.Decoration) {

        companion object {

            val codec = RecordCodecBuilder.mapCodec {
                it.group(
                    Codec.list(ResourceKey.codec(Registries.BIOME))
                        .xmap(MutableList<ResourceKey<Biome>>::toSet, Set<ResourceKey<Biome>>::toList)
                        .fieldOf("biomes").forGetter(Feature::biomes),
                    Codec.list(TagKey.hashedCodec(Registries.BIOME))
                        .xmap(MutableList<TagKey<Biome>>::toSet, Set<TagKey<Biome>>::toList)
                        .fieldOf("tags").forGetter(Feature::tags),
                    Codec.list(ResourceKey.codec(Registries.PLACED_FEATURE))
                        .xmap(MutableList<ResourceKey<PlacedFeature>>::toSet, Set<ResourceKey<PlacedFeature>>::toList)
                        .fieldOf("features").forGetter(Feature::features),
                    GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(Feature::step)
                )
                    .apply(it, ::Feature)
            }

        }

    }

    companion object {

        val codec = Codec.list(Feature.codec.codec()).xmap({ BiomeMod(it) }, { it.features })

    }

}
