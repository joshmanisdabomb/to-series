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

/**
 * The [SavedData] holding every block network of a level, i.e. which blocks are joined to which for each kind of network there is.
 *
 * Networks are not worked out as blocks change, because a single placement can join or split any number of them; instead a change is queued against the position it happened at and every network touching it is rebuilt on the next tick, so that a chain of placements costs one rebuild rather than one each.
 * The networks are kept both by their own ID and by every position they cover, so that a block entity can ask what it belongs to without walking all of them.
 *
 * @param map The networks as they were saved, keyed by their ID.
 * @since 0.6.0
 */
class BlockNetworkSavedData(map: Map<UUID, BlockNetwork>) : SavedData() {

    /**
     * Every network of the level, keyed by its own ID, which is what is written back out when the level saves.
     *
     * @since 0.6.0
     */
    private val networkById = map.toMutableMap()

    /**
     * Every network of the level, keyed by each position it covers, so that a block can find what it belongs to.
     * A position can be in more than one network where the networks are of different kinds.
     *
     * @since 0.6.0
     */
    private val networkByPos = mutableMapOf<BlockPos, MutableList<BlockNetwork>>()

    /**
     * The changes queued since the last tick, which are what decides the networks that get rebuilt on the next one.
     *
     * @since 0.6.0
     */
    private val changes = mutableSetOf<NetworkChange>()

    /**
     * Creates the saved data of a level that has no networks yet.
     *
     * @since 0.6.0
     */
    constructor() : this(mapOf())

    init {
        networkById.values.forEach { network ->
            network.paths.forEach { networkByPos.getOrPut(it) { mutableListOf() }.add(network) }
            network.nodes.forEach { networkByPos.getOrPut(it) { mutableListOf() }.add(network) }
        }
    }

    /**
     * The networks covering a position, of which there can be more than one where they are of different kinds.
     *
     * @param pos The position being asked about.
     * @return The networks covering it, or `null` where none do.
     * @since 0.6.0
     */
    operator fun get(pos: BlockPos): List<BlockNetwork>? = networkByPos[pos]

    /**
     * Queues positions as having changed, so that whatever networks of that kind touch them are rebuilt on the next tick.
     *
     * @param type The kind of network the change affects.
     * @param pos The positions that changed.
     * @since 0.6.0
     */
    fun notify(type: BlockNetworkType, vararg pos: BlockPos) {
        changes.addAll(pos.map { NetworkChange(type, it.immutable()) })
    }

    /**
     * Queues a position and each of its six neighbours as having changed, which is what a block wants when it is placed or broken, since what joins to it has changed as well as the block itself.
     *
     * @param type The kind of network the change affects.
     * @param pos The position that changed.
     * @since 0.6.0
     */
    fun notifyWithNeighbors(type: BlockNetworkType, pos: BlockPos) {
        notify(type, pos, *Direction.entries.map { pos.relative(it) }.toTypedArray())
    }

    /**
     * Rebuilds whatever the queued changes affected and then ticks every network of the level.
     *
     * @param level The level being ticked.
     * @since 0.6.0
     */
    fun tick(level: ServerLevel) {
        if (changes.isNotEmpty()) {
            processChanges(level)
            changes.clear()
            setDirty()
        }
        networkById.values.forEach { it.type.tick(level, it) }
    }

    /**
     * Rebuilds every network affected by the queued changes.
     *
     * Each kind of network is dealt with in turn: the networks touching a changed position are torn down, and then each position that was in one of them is walked out from again, so that a network which was split into several comes back as several and one that was joined comes back as one.
     *
     * @param level The level being rebuilt in.
     * @since 0.6.0
     */
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

    /**
     * Works out one whole network by walking outwards from a position, following the blocks the network runs through and stopping at the ones it only ends at.
     *
     * A block the network runs through is a path and is walked on from, while a block it merely reaches is a node and goes no further, which is what keeps two machines joined by a cable from joining the cables behind them into one network.
     *
     * @param level The level being walked through.
     * @param type The kind of network being built.
     * @param start The position to walk out from.
     * @param visited The positions already covered, which is added to as the walk goes and shared across the whole rebuild so that no position ends up in two networks.
     * @return The network, or `null` where the starting position turned out to run through nothing.
     * @since 0.6.0
     */
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

    /**
     * Takes a network on, recording it against its own ID and against every position it covers.
     *
     * @param network The network to take on.
     * @since 0.6.0
     */
    private fun add(network: BlockNetwork) {
        networkById[network.id] = network
        for (pos in network.positions) {
            networkByPos.getOrPut(pos) { mutableListOf() }.add(network)
        }
    }

    /**
     * Tears a network down, forgetting it both by its ID and at every position it covered.
     *
     * @param network The network to tear down.
     * @since 0.6.0
     */
    private fun remove(network: BlockNetwork) {
        networkById.remove(network.id)
        for (pos in network.positions) {
            val networks = networkByPos[pos] ?: continue
            networks.removeIf { it.id == network.id }
            if (networks.isEmpty()) networkByPos.remove(pos)
        }
    }

    companion object {

        /**
         * The codec the networks of a level are saved and loaded through.
         *
         * @since 0.6.0
         */
        val codec = Codec.unboundedMap(UUIDUtil.STRING_CODEC, BlockNetwork.codec)
            .xmap(::BlockNetworkSavedData, BlockNetworkSavedData::networkById)

    }

    /**
     * One queued change, i.e. a position whose networks of a given kind are to be rebuilt on the next tick.
     *
     * @property type The kind of network the change affects.
     * @property pos The position that changed.
     * @since 0.6.0
     */
    private data class NetworkChange(val type: BlockNetworkType, val pos: BlockPos)

}
