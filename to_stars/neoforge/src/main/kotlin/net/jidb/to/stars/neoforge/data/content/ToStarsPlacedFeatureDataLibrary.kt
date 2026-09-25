package net.jidb.to.stars.neoforge.data.content

import net.jidb.to.base.data.api.library.DatapackLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.RarityFilter

/**
 * [DatapackLibrary] implementation generating the placed features of this mod, i.e. where and how often its ores are generated.
 */
object ToStarsPlacedFeatureDataLibrary : DatapackLibrary<PlacedFeature>(ToStarsMod.modid) {

    override val registryKey = Registries.PLACED_FEATURE

    /**
     * Uranium ore in stone, generated between Y 0 and Y 16 and thickest in the middle of that band, once every six chunks on average.
     */
    val uranium_ore by this {
        PlacedFeature(lookup(ToStarsMod.configuredFeatures.uranium_ore)!!, listOf(
            InSquarePlacement.spread(),
            HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(16)),
            RarityFilter.onAverageOnceEvery(6),
            BiomeFilter.biome(),
        ))
    }

    /**
     * Uranium ore in deepslate, generated between Y -16 and Y 16 and thickest in the middle of that band, once every fourteen chunks on average.
     */
    val deepslate_uranium_ore by this {
        PlacedFeature(lookup(ToStarsMod.configuredFeatures.deepslate_uranium_ore)!!, listOf(
            InSquarePlacement.spread(),
            HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(16)),
            RarityFilter.onAverageOnceEvery(14),
            BiomeFilter.biome(),
        ))
    }

}
