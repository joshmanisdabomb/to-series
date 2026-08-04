package net.jidb.to.base.api.block.network

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.jidb.to.base.ToBaseMod
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.UUIDUtil
import net.minecraft.world.level.block.state.BlockState
import java.util.ArrayDeque
import java.util.Queue
import java.util.UUID

/**
 * Represents a network of interconnected blocks used for defining paths and nodes in a structured format.
 * This data class holds a cache of nodes, paths, and their block states in a specific block network.
 *
 * This cache must be externally updated to remain accurate, see [net.jidb.to.base.pub.block.NetworkCableBlock] for an example.
 *
 * @property id The unique identifier of the block network.
 * @property type The registered type of the block network.
 * @property nodes A set of key positions in the network.
 * @property paths A set of positions that interconnect the nodes within the network.
 * @property states A map representing the block states associated with node and path positions in the network.
 * @since 0.6.0
 */
data class BlockNetwork(val id: UUID, val type: BlockNetworkType, val nodes: Set<BlockPos>, val paths: Set<BlockPos>, val states: Map<BlockPos, BlockState>) {

    /**
     * A combined set of all node and path positions in the block network.
     * @since 0.6.0
     */
    val positions: Set<BlockPos> = nodes + paths

    /**
     * A computed map of routes between nodes and their shortest paths.
     * @since 0.6.0
     */
    val routes: Map<BlockPos, Map<BlockPos, BlockNetworkPath>> by lazy(::computeNodeRoutes)

    /**
     * A computed map of the directions that each position connects to this network.
     * @since 0.6.0
     */
    val connections: Map<BlockPos, Set<Direction>> by lazy { nodes.associateWith { node -> Direction.entries.filter { node.relative(it) in paths }.toSet() } }

    /**
     * Retrieves the shortest path between two positions within the block network.
     *
     * @param from The starting position for the path.
     * @param to The target position for the path.
     * @return The shortest path as a [BlockNetworkPath], or null if no path exists between the specified positions.
     * @since 0.6.0
     */
    fun getShortestPath(from: BlockPos, to: BlockPos) = routes[from]?.get(to)

    /**
     * Retrieves a sorted list of paths from the given node to other nodes in the network, optionally filtered by the given direction.
     *
     * @param node The position of the starting node within the network.
     * @param side The direction to filter the paths by, or null to include paths in all directions.
     * @return A list of paths from this node, in ascending order of the number of intermediate positions.
     * @since 0.6.0
     */
    fun getPathsByDistance(node: BlockPos, side: Direction? = null) = routes[node]?.values?.filter { side == null || it.outgoing == side }?.sortedBy { it.paths.size } ?: emptyList()

    /**
     * Computes the routes between nodes in the block network, determining all possible paths from each node to every other node.
     *
     * @return A map where each key represents a source node as a [BlockPos], and each value is a map that associates target nodes ([BlockPos]) with the computed paths ([BlockNetworkPath]) connecting them.
     * @since 0.6.0
     */
    private fun computeNodeRoutes(): Map<BlockPos, Map<BlockPos, BlockNetworkPath>> {
        val result = mutableMapOf<BlockPos, Map<BlockPos, BlockNetworkPath>>()

        for (sourceNode in nodes) {
            val routesByTarget = mutableMapOf<BlockPos, BlockNetworkPath>()
            val parent = mutableMapOf<BlockPos, BlockPos>()
            val visited = mutableSetOf(sourceNode)
            val queue: Queue<BlockPos> = ArrayDeque()

            for (direction in Direction.entries) {
                val neighbor = sourceNode.relative(direction)
                if (neighbor in paths && visited.add(neighbor)) {
                    parent[neighbor] = sourceNode
                    queue.add(neighbor)
                }
            }

            while (queue.isNotEmpty()) {
                val current = queue.poll()

                for (direction in Direction.entries) {
                    val neighbor = current.relative(direction)
                    if (neighbor in nodes && neighbor != sourceNode && neighbor !in routesByTarget) {
                        val path = mutableListOf(sourceNode)
                        path.addAll(reconstructPath(current, sourceNode, parent))
                        path.add(neighbor)
                        routesByTarget[neighbor] = BlockNetworkPath(path, path.mapNotNull { states[it] })
                    }
                    if (neighbor in paths && visited.add(neighbor)) {
                        parent[neighbor] = current
                        queue.add(neighbor)
                    }
                }
            }

            result[sourceNode] = routesByTarget
        }

        return result
    }

    /**
     * Reconstructs a path from the starting position to the target position using a parent mapping.
     *
     * @param from The starting position of the path.
     * @param until The target position of the path.
     * @param parent A map where each key is a position, and the associated value is the position's parent in the path.
     * @return A list of [BlockPos] representing the reconstructed path in reversed order.
     * @since 0.6.0
     */
    private fun reconstructPath(from: BlockPos, until: BlockPos, parent: Map<BlockPos, BlockPos>): List<BlockPos> {
        val path = mutableListOf<BlockPos>()
        var current = from
        while (current != until) {
            path.add(current)
            current = parent[current] ?: break
        }
        path.reverse()
        return path
    }

    companion object {

        /**
         * A Minecraft codec for instances of the `BlockNetwork` class.
         * @since 0.6.0
         */
        val codec = RecordCodecBuilder.create {
            it.group(
                UUIDUtil.CODEC.fieldOf("id").forGetter(BlockNetwork::id),
                ToBaseMod.registries.block_networks.byNameCodec().fieldOf("type").forGetter(BlockNetwork::type),
                BlockPos.CODEC.listOf().fieldOf("nodes").forGetter { network -> network.nodes.toList() },
                BlockPos.CODEC.listOf().fieldOf("paths").forGetter { network -> network.paths.toList() },
                Codec.unboundedMap(Codec.STRING.comapFlatMap({ DataResult.success(BlockPos.of(it.toLong(16))) }, { it.asLong().toString(16) }), BlockState.CODEC).fieldOf("states").forGetter(BlockNetwork::states),
            )
                .apply(it) { id, type, nodes, paths, states -> BlockNetwork(id, type, nodes.toSet(), paths.toSet(), states) }
        }

    }

}
