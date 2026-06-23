package net.jidb.to.stars.content

import net.jidb.to.base.pub.library.BlockLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.*
import net.jidb.to.stars.info.MachineTier
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

    val atomic_bomb by this { entry -> AtomicBombBlock(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.METAL)
        .requiresCorrectToolForDrops()
        .strength(9.0F, 90.0F)
        .sound(SoundType.METAL)) }

    val copper_machine_enclosure by this { entry -> Block(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_ORANGE)
        .requiresCorrectToolForDrops()
        .strength(4.0F, 5.0F)
        .instrument(NoteBlockInstrument.TRUMPET)
        .sound(SoundType.COPPER_GOLEM_STATUE)) }
    val gold_machine_enclosure by this { entry -> Block(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.GOLD)
        .requiresCorrectToolForDrops()
        .strength(5.0F, 9.0F)
        .instrument(NoteBlockInstrument.BELL)
        .sound(SoundType.NETHERITE_BLOCK)) }

    val copper_power_bank by this { entry -> EnergyStorageBlock(MachineTier.ONE, BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_ORANGE)
        .requiresCorrectToolForDrops()
        .strength(4.0F, 5.0F)
        .instrument(NoteBlockInstrument.TRUMPET)
        .sound(SoundType.COPPER_GOLEM_STATUE)) }
    val gold_power_bank by this { entry -> EnergyStorageBlock(MachineTier.ONE_5, BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.GOLD)
        .requiresCorrectToolForDrops()
        .strength(3.0F, 8.0F)
        .instrument(NoteBlockInstrument.BELL)
        .sound(SoundType.COPPER_BULB)) }

    val power_cable by this { entry -> LossyToEnergyCableBlock(0.02f, BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_ORANGE)
        .requiresCorrectToolForDrops()
        .strength(1.1F, 1.0F)
        .instrument(NoteBlockInstrument.TRUMPET)
        .sound(SoundType.COPPER_GRATE)) }
    val heat_pipe by this { entry -> HeatCableBlock(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.DEEPSLATE)
        .requiresCorrectToolForDrops()
        .strength(3.0F, 8.0F)
        .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
        .sound(SoundType.COPPER_BULB)) }
    val creative_power_source by this { entry -> InfiniteEnergyBlock(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_ORANGE)
        .strength(-1.0F, 3600000.0F)
        .instrument(NoteBlockInstrument.TRUMPET)
        .sound(SoundType.COPPER_BULB)) }

}