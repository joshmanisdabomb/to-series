package net.jidb.to.base.data.api.collection.event

import com.mojang.serialization.Lifecycle
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier

/**
 * The [DatapackDataCollectionEvent] generating the placed features of a mod, i.e. where and how often a configured feature appears.
 *
 * @param context The bootstrap context entries are registered into and looked up through.
 * @see ConfiguredFeatureDataCollectionEvent
 * @since 0.3.0
 */
class PlacedFeatureDataCollectionEvent(context: BootstrapContext<PlacedFeature>) : DatapackDataCollectionEvent<PlacedFeature>(context) {

    /**
     * Registers a placed feature by naming the configured feature it places and the modifiers deciding where it goes, rather than by building the [PlacedFeature] beforehand.
     *
     * @param key The key to register the placed feature under.
     * @param cfeature The key of the configured feature being placed.
     * @param options The placement modifiers deciding where and how often the feature appears.
     * @param lifecycle The lifecycle to register the entry with. Defaults to stable.
     * @throws IllegalStateException If the named configured feature has not been generated.
     * @since 0.3.0
     */
    fun add(key: ResourceKey<PlacedFeature>, cfeature: ResourceKey<ConfiguredFeature<*, *>>, options: List<PlacementModifier>, lifecycle: Lifecycle = Lifecycle.stable()) {
        val registry = lookupRegistry(Registries.CONFIGURED_FEATURE)
        val pfeature = PlacedFeature(registry.getOrThrow(cfeature), options)
        add(key, pfeature, lifecycle)
    }

}
