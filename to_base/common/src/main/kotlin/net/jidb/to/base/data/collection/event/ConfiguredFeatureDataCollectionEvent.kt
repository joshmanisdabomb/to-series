package net.jidb.to.base.data.collection.event

import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature

class ConfiguredFeatureDataCollectionEvent(context: BootstrapContext<ConfiguredFeature<*, *>>) : DatapackDataCollectionEvent<ConfiguredFeature<*, *>>(context)