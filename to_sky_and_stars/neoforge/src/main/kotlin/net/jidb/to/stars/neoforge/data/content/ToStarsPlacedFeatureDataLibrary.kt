package net.jidb.to.stars.neoforge.data.content

import net.jidb.to.base.data.api.library.DatapackLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.placement.*

object ToStarsPlacedFeatureDataLibrary : DatapackLibrary<PlacedFeature>(ToStarsMod.modid) {

    override val registryKey = Registries.PLACED_FEATURE

    val uranium_ore by this {
        PlacedFeature(lookup(ToStarsMod.configuredFeatures.uranium_ore)!!, listOf(
            InSquarePlacement.spread(),
            HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(16)),
            RarityFilter.onAverageOnceEvery(6),
            BiomeFilter.biome(),
        ))
    }
    val deepslate_uranium_ore by this {
        PlacedFeature(lookup(ToStarsMod.configuredFeatures.deepslate_uranium_ore)!!, listOf(
            InSquarePlacement.spread(),
            HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(16)),
            RarityFilter.onAverageOnceEvery(14),
            BiomeFilter.biome(),
        ))
    }

}
