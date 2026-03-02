package net.jidb.to.stars.content

import net.jidb.to.base.block.HorizontalBlock
import net.jidb.to.base.library.SimpleRegistryLibrary
import net.jidb.to.stars.ToSkyAndStarsMod
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor

object ToSkyAndStarsBlockLibrary : SimpleRegistryLibrary<Block>(ToSkyAndStarsMod.MOD_ID) {

    override val registry = BuiltInRegistries.BLOCK

    val test_block by this { entry -> HorizontalBlock(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_BLUE)
        .strength(0.5f)
        .sound(SoundType.METAL)) }

}