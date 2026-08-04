package net.jidb.to.base.api.block.network

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState

/**
 * Represents a path within a block network, including start and end points, intermediate nodes, and cached block states for these positions.
 *
 * @property path A list of positions defining the path, starting at the origin and ending at the destination.
 * @property states A list of block states corresponding to each position in the path, associated 1:1 with each value in `path` at the same index.
 * @constructor Creates a new instance of `BlockNetworkPath` with the specified path and block states.
 * @since 0.6.0
 */
data class BlockNetworkPath(val path: List<BlockPos>, val states: List<BlockState>) {

    /**
     * The starting position of the path. This is the first element in the `path` list.
     *
     * @since 0.6.0
     */
    val from = path.first()

    /**
     * The ending position of the path. This is the last element in the `path` list.
     *
     * @since 0.6.0
     */
    val to = path.last()

    /**
     * The middle path positions of this [BlockNetworkPath], excluding the first and last position (nodes).
     *
     * @since 0.6.0
     */
    val paths = path.drop(1).dropLast(1)

    /**
     * The outgoing [Direction] from the first position ([from]) to the second position (first position in [paths])
     *
     * @since 0.6.0
     */
    val outgoing = Direction.entries.firstOrNull { it.unitVec3i == (paths.firstOrNull() ?: to).subtract(from) }

    /**
     * The incoming [Direction] from the penultimate position (last position in [paths]) to the last position ([to]).
     *
     * @since 0.6.0
     */
    val incoming = Direction.entries.firstOrNull { it.unitVec3i == to.subtract(paths.lastOrNull() ?: from) }

}
