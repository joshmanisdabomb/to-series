package net.jidb.to.base.api.block.network

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.jidb.to.base.ToBaseMod
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.UUIDUtil
import net.minecraft.world.level.block.state.BlockState
import java.util.*

data class BlockNetwork(val id: UUID, val type: BlockNetworkType, val nodes: Set<BlockPos>, val paths: Set<BlockPos>, val states: Map<BlockPos, BlockState>) {

    val positions: Set<BlockPos> = nodes + paths
    val routes: Map<BlockPos, Map<BlockPos, BlockNetworkPath>> by lazy(::computeNodeRoutes)
    val connections: Map<BlockPos, Set<Direction>> by lazy { nodes.associateWith { node -> Direction.entries.filter { node.relative(it) in paths }.toSet() } }

    fun getShortestPath(from: BlockPos, to: BlockPos): BlockNetworkPath? {
        return routes[from]?.get(to)
    }

    fun getPathsByDistance(node: BlockPos, side: Direction? = null): List<BlockNetworkPath> {
        return routes[node]?.values?.filter { side == null || it.outgoing == side }?.sortedBy { it.paths.size } ?: emptyList()
    }

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