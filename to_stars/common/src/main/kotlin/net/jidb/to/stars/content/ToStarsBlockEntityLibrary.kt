package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.AtomicBombBlockEntity
import net.jidb.to.stars.block.entity.BoilingCauldronBlockEntity
import net.jidb.to.stars.block.entity.CentrifugeBlockEntity
import net.jidb.to.stars.block.entity.EnergyStorageBlockEntity
import net.jidb.to.stars.block.entity.KilnBlockEntity
import net.jidb.to.stars.block.entity.RotorBlockEntity
import net.jidb.to.stars.block.entity.SolidGeneratorBlockEntity
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType

/**
 * [SimpleRegistryLibrary] implementation holding the block entity types of this mod, i.e. the blocks that keep something of their own between ticks.
 */
object ToStarsBlockEntityLibrary : SimpleRegistryLibrary<BlockEntityType<*>>(ToStarsMod.modid) {

    override val registry = BuiltInRegistries.BLOCK_ENTITY_TYPE

    /**
     * The block entity of the atomic bomb, which counts down to its detonation.
     */
    val atomic_bomb by this { Services.platform.blocks.createBlockEntityType(::AtomicBombBlockEntity, ToStarsMod.blocks.atomic_bomb) }

    /**
     * The block entity of a power bank, shared by every tier of one.
     */
    val power_bank by this { Services.platform.blocks.createBlockEntityType(::EnergyStorageBlockEntity, ToStarsMod.blocks.copper_power_bank, ToStarsMod.blocks.gold_power_bank) }

    /**
     * The block entity of a solid generator, shared by every tier of one.
     */
    val solid_generator by this { Services.platform.blocks.createBlockEntityType(::SolidGeneratorBlockEntity, ToStarsMod.blocks.copper_solid_generator, ToStarsMod.blocks.gold_solid_generator) }

    /**
     * The block entity of the boiler, which holds the water it heats.
     */
    val boiler by this { Services.platform.blocks.createBlockEntityType(::BoilingCauldronBlockEntity, ToStarsMod.blocks.boiler) }

    /**
     * The block entity of the rotor blades, which keeps how fast they are turning.
     */
    val rotor_blades by this { Services.platform.blocks.createBlockEntityType(::RotorBlockEntity, ToStarsMod.blocks.rotor_blades) }

    /**
     * The block entity of a centrifuge, shared by every tier of one.
     */
    val centrifuge by this { Services.platform.blocks.createBlockEntityType(::CentrifugeBlockEntity, ToStarsMod.blocks.copper_centrifuge, ToStarsMod.blocks.gold_centrifuge) }

    /**
     * The block entity for the kiln, that handles the smelting routine.
     * @see ToStarsBlockLibrary.kiln
     * @see ToStarsMenuLibrary.kiln
     * @see net.jidb.to.stars.client.content.ToStarsScreenLibrary.kiln
     */
    val kiln by this { Services.platform.blocks.createBlockEntityType(::KilnBlockEntity, ToStarsMod.blocks.kiln) }

}
