package net.jidb.to.base.api.block.network

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState

/**
 * A type that defines how [BlockNetwork]s can be formed and handles their ticking.
 * These types are to be registered to the [net.minecraft.core.Registry] at [net.jidb.to.base.pub.ToBaseRegistryLibrary.block_networks].
 *
 * @since 0.6.0
 */
abstract class BlockNetworkType {

    /**
     * Checks if the block at the given position can be added to the network.
     *
     * @param level The level to check the block in.
     * @param pos The position of the block to check.
     * @param state The state of the block to check.
     * @param from The direction the block is being added from, or null if it is not being added from a direction.
     * @return the type of position to add to the network, or `null` to not add this position to the network.
     * @since 0.6.0
     */
    abstract fun getPositionType(level: LevelReader, pos: BlockPos, state: BlockState, from: Direction? = null): BlockNetworkPositionType?

    /**
     * Performs a single tick update for the specified block network within the given server level.
     *
     * @param level The server level where the block network resides.
     * @param network The block network to tick.
     * @since 0.6.0
     */
    abstract fun tick(level: ServerLevel, network: BlockNetwork)

}
