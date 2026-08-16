package net.jidb.to.base.hooks.level.storage

import com.mojang.serialization.Codec
import net.jidb.to.base.api.block.network.BlockNetwork
import net.jidb.to.base.api.block.network.BlockNetworkPositionType
import net.jidb.to.base.api.block.network.BlockNetworkType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.UUIDUtil
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.saveddata.SavedData
import java.util.ArrayDeque
import java.util.Queue
import java.util.UUID

class BlockNetworkSavedData(map: Map<UUID, BlockNetwork>) : SavedData() {

    private val networkById = map.toMutableMap()
    private val networkByPos = mutableMapOf<BlockPos, MutableList<BlockNetwork>>()
    private val changes = mutableSetOf<NetworkChange>()

    constructor() : this(mapOf())

    init {
        networkById.values.forEach { network ->
            network.paths.forEach { networkByPos.getOrPut(it) { mutableListOf() }.add(network) }
            network.nodes.forEach { networkByPos.getOrPut(it) { mutableListOf() }.add(network) }
        }
    }

    operator fun get(pos: BlockPos): List<BlockNetwork>? = networkByPos[pos]

    fun notify(type: BlockNetworkType, vararg pos: BlockPos) {
        changes.addAll(pos.map { NetworkChange(type, it.immutable()) })
    }

    fun notifyWithNeighbors(type: BlockNetworkType, pos: BlockPos) {
        notify(type, pos, *Direction.entries.map { pos.relative(it) }.toTypedArray())
    }

    fun tick(level: ServerLevel) {
        if (changes.isNotEmpty()) {
            processChanges(level)
            changes.clear()
            setDirty()
        }
        networkById.values.forEach { it.type.tick(level, it) }
    }

    private fun processChanges(level: ServerLevel) {
        val changesByType = changes.groupBy({ it.type }, { it.pos })

        for ((type, positions) in changesByType) {
            val affectedNetworks = mutableSetOf<BlockNetwork>()
            for (pos in positions) {
                networkByPos[pos]?.filter { it.type == type }?.let { affectedNetworks.addAll(it) }
            }

            val affected = mutableSetOf<BlockPos>()
            for (network in affectedNetworks) {
                affected.addAll(network.positions)
                remove(network)
            }
            affected.addAll(positions)

            val visited = mutableSetOf<BlockPos>()
            for (startPos in affected) {
                if (startPos in visited) continue
                val state = level.getBlockState(startPos)
                if (type.getPositionType(level, startPos, state) != BlockNetworkPositionType.PATH) continue

                val network = calculate(level, type, startPos, visited)
                if (network != null) add(network)
            }
        }
    }

    private fun calculate(level: ServerLevel, type: BlockNetworkType, start: BlockPos, visited: MutableSet<BlockPos>): BlockNetwork? {
        val paths = mutableSetOf<BlockPos>()
        val nodes = mutableSetOf<BlockPos>()
        val states = mutableMapOf<BlockPos, BlockState>()
        val queue: Queue<Pair<BlockPos, Direction?>> = ArrayDeque()

        queue.add(start to null)
        while (queue.isNotEmpty()) {
            val (pos, from) = queue.poll()

            val state = level.getBlockState(pos)
            when (type.getPositionType(level, pos, state, from)) {
                BlockNetworkPositionType.PATH -> {
                    if (!visited.add(pos)) continue
                    paths.add(pos)
                    states[pos] = state
                    for (direction in Direction.entries) {
                        val neighbor = pos.relative(direction)
                        if (neighbor !in visited) queue.add(neighbor to direction.opposite)
                    }
                }
                BlockNetworkPositionType.NODE -> {
                    nodes.add(pos)
                    states[pos] = state
                }
                null -> {}
            }
        }

        if (paths.isEmpty()) return null
        return BlockNetwork(UUID.randomUUID(), type, nodes, paths, states)
    }

    private fun add(network: BlockNetwork) {
        networkById[network.id] = network
        for (pos in network.positions) {
            networkByPos.getOrPut(pos) { mutableListOf() }.add(network)
        }
    }

    private fun remove(network: BlockNetwork) {
        networkById.remove(network.id)
        for (pos in network.positions) {
            val networks = networkByPos[pos] ?: continue
            networks.removeIf { it.id == network.id }
            if (networks.isEmpty()) networkByPos.remove(pos)
        }
    }

    companion object {

        val codec = Codec.unboundedMap(UUIDUtil.STRING_CODEC, BlockNetwork.codec)
            .xmap(::BlockNetworkSavedData, BlockNetworkSavedData::networkById)

    }

    private data class NetworkChange(val type: BlockNetworkType, val pos: BlockPos)

}
