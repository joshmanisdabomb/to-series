package net.jidb.to.stars.content

import net.jidb.to.base.library.SimpleRegistryLibrary
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.AtomicBombBlockEntity
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType

object ToStarsBlockEntityLibrary : SimpleRegistryLibrary<BlockEntityType<*>>(ToStarsMod.modid) {

    override val registry = BuiltInRegistries.BLOCK_ENTITY_TYPE

    val atomic_bomb by this { Services.platform.blocks.createBlockEntityType(::AtomicBombBlockEntity, ToStarsMod.blocks.atomic_bomb) }

}
