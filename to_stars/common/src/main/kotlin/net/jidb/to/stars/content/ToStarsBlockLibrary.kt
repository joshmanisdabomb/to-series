package net.jidb.to.stars.content

import net.jidb.to.base.api.helper.KotlinHelper.either
import net.jidb.to.base.pub.library.BlockLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.AtomicBombBlock
import net.jidb.to.stars.block.BoilingCauldronBlock
import net.jidb.to.stars.block.CentrifugeBlock
import net.jidb.to.stars.block.EnergyStorageBlock
import net.jidb.to.stars.block.HeatCableBlock
import net.jidb.to.stars.block.InfiniteEnergyBlock
import net.jidb.to.stars.block.LossyToEnergyCableBlock
import net.jidb.to.stars.block.NuclearFireBlock
import net.jidb.to.stars.block.NuclearWasteBlock
import net.jidb.to.stars.block.RotorBlock
import net.jidb.to.stars.block.SolidGeneratorBlock
import net.jidb.to.stars.block.TurbineBlock
import net.jidb.to.stars.info.MachineTier
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.util.ColorRGBA
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DropExperienceBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction

/**
 * [BlockLibrary] implementation holding every block of this mod.
 */
object ToStarsBlockLibrary : BlockLibrary(ToStarsMod.MOD_ID) {

    override val registry = BuiltInRegistries.BLOCK

    /**
     * What a nuclear explosion leaves behind, which spreads and decays over time.
     */
    val nuclear_waste by this { entry -> NuclearWasteBlock(ColorRGBA(0xFF544F4E.toInt()), BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.TERRACOTTA_CYAN)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .requiresCorrectToolForDrops()
        .strength(30.0f, 8.0f)
        .randomTicks()
        .sound(SoundType.CORAL_BLOCK)) }

    /**
     * The fire a nuclear explosion burns with, which is brighter and longer-lived than an ordinary one.
     */
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

    /**
     * Uranium ore as it is found in stone.
     */
    val uranium_ore by this { entry -> DropExperienceBlock(UniformInt.of(2, 5), BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.STONE)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .requiresCorrectToolForDrops()
        .lightLevel { 1 }
        .strength(3.0F, 3.0F)
        .sound(SoundType.STONE)) }

    /**
     * Uranium ore as it is found in deepslate, which takes longer to mine.
     */
    val deepslate_uranium_ore by this { entry -> DropExperienceBlock(UniformInt.of(2, 5), BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.DEEPSLATE)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .requiresCorrectToolForDrops()
        .lightLevel { 1 }
        .strength(4.5F, 3.0F)
        .sound(SoundType.DEEPSLATE)) }

    /**
     * Uranium compacted into a block.
     */
    val uranium_block by this { entry -> Block(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_LIGHT_GREEN)
        .instrument(NoteBlockInstrument.DIDGERIDOO)
        .requiresCorrectToolForDrops()
        .lightLevel { 1 }
        .strength(5.0F, 6.0F)
        .sound(SoundType.STONE)) }

    /**
     * Enriched uranium compacted into a block, which glows more brightly than plain uranium.
     */
    val enriched_uranium_block by this { entry -> Block(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_LIGHT_GREEN)
        .instrument(NoteBlockInstrument.DIDGERIDOO)
        .requiresCorrectToolForDrops()
        .lightLevel { 3 }
        .strength(5.0F, 6.0F)
        .sound(SoundType.STONE)) }

    /**
     * Heavy uranium compacted into a block.
     */
    val heavy_uranium_block by this { entry -> Block(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_GREEN)
        .instrument(NoteBlockInstrument.DIDGERIDOO)
        .requiresCorrectToolForDrops()
        .strength(5.0F, 6.0F)
        .sound(SoundType.STONE)) }

    /**
     * Heavy uranium worked into shielding, which is tougher than the plain block and stops a nuclear blast.
     */
    val heavy_uranium_shielding by this { entry -> Block(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_GREEN)
        .instrument(NoteBlockInstrument.DIDGERIDOO)
        .requiresCorrectToolForDrops()
        .strength(7.5F, 14.0F)
        .sound(SoundType.FUNGUS)) }

    /**
     * The atomic bomb, which is armed through its own interface and then counts down to a nuclear explosion.
     */
    val atomic_bomb by this { entry -> AtomicBombBlock(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.METAL)
        .requiresCorrectToolForDrops()
        .strength(9.0F, 90.0F)
        .sound(SoundType.METAL)) }

    /**
     * The tier one machine enclosure, an intermediate step in crafting the machines of that tier.
     */
    val copper_machine_enclosure by this { entry -> Block(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_ORANGE)
        .requiresCorrectToolForDrops()
        .strength(4.0F, 5.0F)
        .instrument(NoteBlockInstrument.TRUMPET)
        .sound(SoundType.COPPER_GOLEM_STATUE)) }

    /**
     * The tier one and a half machine enclosure, an intermediate step in crafting the machines of that tier.
     */
    val gold_machine_enclosure by this { entry -> Block(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.GOLD)
        .requiresCorrectToolForDrops()
        .strength(5.0F, 9.0F)
        .instrument(NoteBlockInstrument.BELL)
        .sound(SoundType.NETHERITE_BLOCK)) }

    /**
     * The tier one power bank, which stores To Energy.
     */
    val copper_power_bank by this { entry -> EnergyStorageBlock(MachineTier.ONE, BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_ORANGE)
        .requiresCorrectToolForDrops()
        .strength(4.0F, 5.0F)
        .instrument(NoteBlockInstrument.TRUMPET)
        .sound(SoundType.COPPER_GOLEM_STATUE)) }

    /**
     * The tier one and a half power bank, which stores more To Energy than the copper one.
     */
    val gold_power_bank by this { entry -> EnergyStorageBlock(MachineTier.ONE_5, BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.GOLD)
        .requiresCorrectToolForDrops()
        .strength(3.0F, 8.0F)
        .instrument(NoteBlockInstrument.BELL)
        .sound(SoundType.COPPER_BULB)) }

    /**
     * The tier one solid generator, which burns fuel to make To Energy and lights up while it does.
     */
    val copper_solid_generator by this { entry -> SolidGeneratorBlock(MachineTier.ONE, BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_ORANGE)
        .requiresCorrectToolForDrops()
        .lightLevel { it.getValue(BlockStateProperties.LIT).either(13, 0) }
        .strength(4.0F, 5.0F)
        .isValidSpawn { state, blockGetter, blockPos, entityType -> entityType.fireImmune() }
        .instrument(NoteBlockInstrument.TRUMPET)
        .sound(SoundType.COPPER_GOLEM_STATUE)) }

    /**
     * The tier one and a half solid generator, which burns fuel faster than the copper one.
     */
    val gold_solid_generator by this { entry -> SolidGeneratorBlock(MachineTier.ONE_5, BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.GOLD)
        .requiresCorrectToolForDrops()
        .lightLevel { it.getValue(BlockStateProperties.LIT).either(13, 0) }
        .strength(5.0F, 9.0F)
        .isValidSpawn { state, blockGetter, blockPos, entityType -> entityType.fireImmune() }
        .instrument(NoteBlockInstrument.BELL)
        .sound(SoundType.NETHERITE_BLOCK)) }

    /**
     * The boiler, which heats the water it holds and scalds anything standing in it.
     */
    val boiler by this { entry -> BoilingCauldronBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON)
        .setId(getEntryResourceKey(entry))) }

    /**
     * The tier one turbine, which makes To Energy from the rotor blades turning above it.
     */
    val copper_turbine by this { entry -> TurbineBlock(MachineTier.ONE, BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_ORANGE)
        .requiresCorrectToolForDrops()
        .lightLevel { it.getValue(BlockStateProperties.LIT).either(5, 0) }
        .strength(4.0F, 5.0F)
        .instrument(NoteBlockInstrument.TRUMPET)
        .sound(SoundType.COPPER_GOLEM_STATUE)) }

    /**
     * The tier one and a half turbine, which makes more To Energy than the copper one.
     */
    val gold_turbine by this { entry -> TurbineBlock(MachineTier.ONE_5, BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.GOLD)
        .requiresCorrectToolForDrops()
        .lightLevel { it.getValue(BlockStateProperties.LIT).either(5, 0) }
        .strength(5.0F, 9.0F)
        .instrument(NoteBlockInstrument.BELL)
        .sound(SoundType.NETHERITE_BLOCK)) }

    /**
     * The rotor blades a turbine is driven by, which cut anything that walks into them while they are turning.
     */
    val rotor_blades by this { entry -> RotorBlock(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.DEEPSLATE)
        .requiresCorrectToolForDrops()
        .strength(3.0F, 8.0F)
        .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
        .sound(SoundType.METAL)) }

    /**
     * The tier one centrifuge, which processes uranium into its enriched and heavy forms.
     */
    val copper_centrifuge by this { entry -> CentrifugeBlock(MachineTier.ONE, BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_ORANGE)
        .requiresCorrectToolForDrops()
        .strength(4.0F, 5.0F)
        .instrument(NoteBlockInstrument.TRUMPET)
        .sound(SoundType.COPPER_GOLEM_STATUE)) }

    /**
     * The tier one and a half centrifuge, which processes faster than the copper one.
     */
    val gold_centrifuge by this { entry -> CentrifugeBlock(MachineTier.ONE_5, BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.GOLD)
        .requiresCorrectToolForDrops()
        .strength(5.0F, 9.0F)
        .instrument(NoteBlockInstrument.BELL)
        .sound(SoundType.NETHERITE_BLOCK)) }

    /**
     * The power cable, which carries To Energy between machines and loses a fiftieth of what passes through it.
     */
    val power_cable by this { entry -> LossyToEnergyCableBlock(0.02f, BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_ORANGE)
        .requiresCorrectToolForDrops()
        .strength(1.1F, 1.0F)
        .instrument(NoteBlockInstrument.TRUMPET)
        .sound(SoundType.COPPER_GRATE)) }

    /**
     * The heat pipe, which carries heat between machines.
     */
    val heat_pipe by this { entry -> HeatCableBlock(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.DEEPSLATE)
        .requiresCorrectToolForDrops()
        .strength(3.0F, 8.0F)
        .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
        .sound(SoundType.COPPER_BULB)) }

    /**
     * A block giving out endless To Energy, for testing and for creative mode. It cannot be broken by anything but a creative player.
     */
    val creative_power_source by this { entry -> InfiniteEnergyBlock(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_ORANGE)
        .strength(-1.0F, 3600000.0F)
        .instrument(NoteBlockInstrument.TRUMPET)
        .sound(SoundType.COPPER_BULB)) }

}
