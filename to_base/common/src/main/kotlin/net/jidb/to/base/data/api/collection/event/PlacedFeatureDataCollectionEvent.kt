package net.jidb.to.base.data.api.collection.event

import com.mojang.serialization.Lifecycle
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier

class PlacedFeatureDataCollectionEvent(context: BootstrapContext<PlacedFeature>) : DatapackDataCollectionEvent<PlacedFeature>(context) {

    fun add(key: ResourceKey<PlacedFeature>, cfeature: ResourceKey<ConfiguredFeature<*, *>>, options: List<PlacementModifier>, lifecycle: Lifecycle = Lifecycle.stable()) {
        val registry = lookupRegistry(Registries.CONFIGURED_FEATURE)
        val pfeature = PlacedFeature(registry.getOrThrow(cfeature), options)
        add(key, pfeature, lifecycle)
    }

}
