package net.jidb.to.base.api.platform

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

/**
 * A [Platform]-specific module that defines a cross-platform contract for handling blocks and block entities.
 * This module has code for creating a [BlockEntityType].
 *
 * @since 0.1.0
 */
abstract class BlocksPlatformModule {

    /**
     * Creates a new [BlockEntityType] for the provided block entity class.
     *
     * The constructor is inaccessible in vanilla code, but modloaders make it accessible separately.
     *
     * @param T The type of the block entity.
     * @param factory A function that produces a new block entity instance, given the block position and state.
     * @param blocks The blocks that are associated with the block entity type.
     * @param opNbt Optional flag indicating if the block entity supports NBT operations when a player is 'op'. Defaults to false.
     * @return A new [BlockEntityType] instance for the provided block entity type and associated blocks.
     *
     * @since 0.4.0
     */
    abstract fun <T : BlockEntity> createBlockEntityType(factory: (pos: BlockPos, state: BlockState) -> T, vararg blocks: Block, opNbt: Boolean = false): BlockEntityType<T>

}
