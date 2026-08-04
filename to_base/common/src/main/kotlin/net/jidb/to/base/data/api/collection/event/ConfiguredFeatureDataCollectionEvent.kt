package net.jidb.to.base.data.api.collection.event

import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature

/**
 * The [DatapackDataCollectionEvent] generating the configured features of a mod, i.e. what a piece of world generation looks like rather than where it goes.
 *
 * @param context The bootstrap context entries are registered into and looked up through.
 * @see PlacedFeatureDataCollectionEvent
 * @since 0.3.0
 */
class ConfiguredFeatureDataCollectionEvent(context: BootstrapContext<ConfiguredFeature<*, *>>) : DatapackDataCollectionEvent<ConfiguredFeature<*, *>>(context)
