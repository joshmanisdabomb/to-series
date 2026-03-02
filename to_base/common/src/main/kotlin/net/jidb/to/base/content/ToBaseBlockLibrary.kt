package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.block.HorizontalBlock
import net.jidb.to.base.library.SimpleRegistryLibrary
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor

object ToBaseBlockLibrary : SimpleRegistryLibrary<Block>(ToBaseMod.MOD_ID) {

    override val registry = BuiltInRegistries.BLOCK

    val test_block by this { entry -> Block(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_YELLOW)
        .strength(0.5f)
        .sound(SoundType.SCAFFOLDING)) }
    val test_block_2 by this { entry -> HorizontalBlock(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_YELLOW)
        .strength(0.5f)
        .sound(SoundType.SCAFFOLDING)) }

}