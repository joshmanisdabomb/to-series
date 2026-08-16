package net.jidb.to.stars.neoforge.data.content

import net.jidb.to.base.data.api.library.DatapackLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest

object ToStarsConfiguredFeatureDataLibrary : DatapackLibrary<ConfiguredFeature<*, *>>(ToStarsMod.modid) {

    override val registryKey = Registries.CONFIGURED_FEATURE

    val uranium_ore by this { ConfiguredFeature(Feature.SCATTERED_ORE, OreConfiguration(
        listOf(
            OreConfiguration.target(TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ToStarsMod.blocks.uranium_ore.defaultBlockState()),
        ),
        5,
        1f
    )) }

    val deepslate_uranium_ore by this { ConfiguredFeature(Feature.SCATTERED_ORE, OreConfiguration(
        listOf(
            OreConfiguration.target(TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ToStarsMod.blocks.deepslate_uranium_ore.defaultBlockState()),
        ),
        3,
        0f
    )) }

}
