package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.block.HorizontalGenericBlock
import net.jidb.to.base.block.properties.ExtendedBlockProperties
import net.jidb.to.base.content.block.ResearchDeskBlock
import net.jidb.to.base.library.BlockLibrary
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor

object ToBaseBlockLibrary : BlockLibrary(ToBaseMod.modid) {

    override val registry = BuiltInRegistries.BLOCK

    val test_block by this { entry -> Block(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_YELLOW)
        .strength(0.5f)
        .sound(SoundType.SCAFFOLDING)) }
    val test_block_2 by this { entry -> HorizontalGenericBlock(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_YELLOW)
        .strength(0.5f)
        .sound(SoundType.SCAFFOLDING)) }

    val research_desk by this { entry -> ResearchDeskBlock(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.WOOD)
        .forceSolidOn()
        .instrument(NoteBlockInstrument.BASS)
        .strength(2.5F)
        .sound(SoundType.WOOD)
        .ignitedByLava()) }
        .tag(properties, ExtendedBlockProperties()
            .flammable())

}