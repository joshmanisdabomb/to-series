package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.*
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType

object ToStarsBlockEntityLibrary : SimpleRegistryLibrary<BlockEntityType<*>>(ToStarsMod.modid) {

    override val registry = BuiltInRegistries.BLOCK_ENTITY_TYPE

    val atomic_bomb by this { Services.platform.blocks.createBlockEntityType(::AtomicBombBlockEntity, ToStarsMod.blocks.atomic_bomb) }
    val power_bank by this { Services.platform.blocks.createBlockEntityType(::EnergyStorageBlockEntity, ToStarsMod.blocks.copper_power_bank, ToStarsMod.blocks.gold_power_bank) }
    val solid_generator by this { Services.platform.blocks.createBlockEntityType(::SolidGeneratorBlockEntity, ToStarsMod.blocks.copper_solid_generator, ToStarsMod.blocks.gold_solid_generator) }
    val boiler by this { Services.platform.blocks.createBlockEntityType(::BoilingCauldronBlockEntity, ToStarsMod.blocks.boiler) }
    val rotor_blades by this { Services.platform.blocks.createBlockEntityType(::RotorBlockEntity, ToStarsMod.blocks.rotor_blades) }
    val centrifuge by this { Services.platform.blocks.createBlockEntityType(::CentrifugeBlockEntity, ToStarsMod.blocks.copper_centrifuge, ToStarsMod.blocks.gold_centrifuge) }

}
