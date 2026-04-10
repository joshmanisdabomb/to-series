package net.jidb.to.stars.content

import net.jidb.to.base.block.properties.ExtendedBlockProperties
import net.jidb.to.base.library.BlockLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.NuclearFireBlock
import net.jidb.to.stars.block.NuclearWasteBlock
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.util.ColorRGBA
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DropExperienceBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction

object ToStarsBlockLibrary : BlockLibrary(ToStarsMod.MOD_ID) {

    override val registry = BuiltInRegistries.BLOCK

    val nuclear_waste by this { entry -> NuclearWasteBlock(ColorRGBA(0xFF544F4E.toInt()), BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.TERRACOTTA_CYAN)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .requiresCorrectToolForDrops()
        .strength(30.0f, 8.0f)
        .randomTicks()
        .sound(SoundType.CORAL_BLOCK)) }
    val nuclear_fire by this { entry -> NuclearFireBlock(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_LIGHT_GREEN)
        .randomTicks()
        .replaceable()
        .noCollision()
        .instabreak()
        .lightLevel { 15 }
        .sound(SoundType.SCULK_SENSOR)
        .pushReaction(PushReaction.DESTROY)) }
        .tag(properties, ExtendedBlockProperties()
            .renderLayer(ExtendedBlockProperties.RenderLayer.CUTOUT))

    val uranium_ore by this { entry -> DropExperienceBlock(UniformInt.of(2, 5), BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.STONE)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .requiresCorrectToolForDrops()
        .lightLevel { 1 }
        .strength(3.0F, 3.0F)
        .sound(SoundType.STONE)) }
    val deepslate_uranium_ore by this { entry -> DropExperienceBlock(UniformInt.of(2, 5), BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.DEEPSLATE)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .requiresCorrectToolForDrops()
        .lightLevel { 1 }
        .strength(4.5F, 3.0F)
        .sound(SoundType.DEEPSLATE)) }
    val uranium_block by this { entry -> Block(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_LIGHT_GREEN)
        .instrument(NoteBlockInstrument.DIDGERIDOO)
        .requiresCorrectToolForDrops()
        .lightLevel { 1 }
        .strength(5.0F, 6.0F)
        .sound(SoundType.STONE)) }
    val enriched_uranium_block by this { entry -> Block(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_LIGHT_GREEN)
        .instrument(NoteBlockInstrument.DIDGERIDOO)
        .requiresCorrectToolForDrops()
        .lightLevel { 3 }
        .strength(5.0F, 6.0F)
        .sound(SoundType.STONE)) }
    val heavy_uranium_block by this { entry -> Block(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_GREEN)
        .instrument(NoteBlockInstrument.DIDGERIDOO)
        .requiresCorrectToolForDrops()
        .strength(5.0F, 6.0F)
        .sound(SoundType.STONE)) }
    val heavy_uranium_shielding by this { entry -> Block(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_GREEN)
        .instrument(NoteBlockInstrument.DIDGERIDOO)
        .requiresCorrectToolForDrops()
        .strength(7.5F, 14.0F)
        .sound(SoundType.FUNGUS)) }

}