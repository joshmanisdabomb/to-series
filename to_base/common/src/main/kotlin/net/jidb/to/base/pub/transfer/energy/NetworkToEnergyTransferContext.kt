package net.jidb.to.base.pub.transfer.energy

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.block.network.BlockNetwork
import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level

/**
 * The [ToEnergyTransferContext] for a cable network, which stores nothing itself and passes everything on to the blocks attached to it.
 * Inserting into the network means finding somewhere on it with room, and extracting from it means finding somewhere on it with energy to give, so both are carried out over the network's own nodes.
 *
 * Where a node is given, only the routes leading away from that node are considered, and they are tried nearest first; otherwise every connection on the network is tried in turn.
 * Each attempt runs in a nested transaction that is only committed once something has actually moved, so a route that turns out to lead nowhere leaves no trace.
 *
 * @property network The network the energy is travelling over.
 * @property level The level the network is in.
 * @property node The node the energy is entering or leaving the network at, or `null` to consider the whole network. Defaults to `null`.
 * @since 0.6.0
 */
class NetworkToEnergyTransferContext(val network: BlockNetwork, val level: Level, val node: NodeSide? = null) : ToEnergyTransferContext {

    override fun getSlotCount() = 1

    override fun getAmountAt(index: Int) = 0L

    override fun getCapacityAt(resource: Unit, index: Int) = Long.MAX_VALUE

    override fun insert(resource: Unit, amount: Long, transaction: TransferTransaction): Long {
        transaction.openNested().use {
            return if (node != null) {
                runOnPaths(node, amount, false, it)
            } else {
                runOnConnections(amount, false, it)
            }
        }
    }

    override fun extract(resource: Unit, amount: Long, transaction: TransferTransaction): Long {
        transaction.openNested().use {
            return if (node != null) {
                runOnPaths(node, amount, true, it)
            } else {
                runOnConnections(amount, true, it)
            }
        }
    }

    /**
     * Moves energy along the routes leading away from a single node, nearest first, until the amount runs out or the routes do.
     * A route into unloaded chunks is skipped rather than loading them.
     *
     * @param node The node the energy is entering or leaving the network at.
     * @param amount The amount of energy to move.
     * @param extract Whether the energy is being pulled off the network rather than pushed onto it.
     * @param transaction The transaction the movement is recorded in.
     * @return The amount of energy that was actually moved.
     * @since 0.6.0
     */
    private fun runOnPaths(node: NodeSide, amount: Long, extract: Boolean, transaction: TransferTransaction): Long {
        var left = amount
        val paths = network.getPathsByDistance(node.pos, node.side)
        for (path in paths) {
            if (!level.isLoaded(path.to)) continue

            transaction.openNested().use {
                val context = ToBaseMod.transferProviders.to_energy.fromBlock(level, path.to, path.incoming?.opposite) ?: continue
                left = move(left, extract, context, path.paths, it)
            }

            if (left <= 0) {
                transaction.commit()
                return amount
            }
        }

        if (left < amount) {
            transaction.commit()
        }
        return amount - left
    }

    /**
     * Moves energy over every connection on the network in turn, until the amount runs out or the connections do.
     * This is the case where no node was named, so there is no distance to order the attempts by.
     *
     * @param amount The amount of energy to move.
     * @param extract Whether the energy is being pulled off the network rather than pushed onto it.
     * @param transaction The transaction the movement is recorded in.
     * @return The amount of energy that was actually moved.
     * @since 0.6.0
     */
    private fun runOnConnections(amount: Long, extract: Boolean, transaction: TransferTransaction): Long {
        var left = amount
        for ((node, sides) in network.connections) {
            if (!level.isLoaded(node)) continue

            for (side in sides) {
                transaction.openNested().use {
                    val context = ToBaseMod.transferProviders.to_energy.fromBlock(level, node, side) ?: continue
                    left = move(left, extract, context, network.paths, it)
                }

                if (left <= 0) {
                    transaction.commit()
                    return amount
                }
            }
        }

        if (left < amount) {
            transaction.commit()
        }
        return amount - left
    }

    /**
     * Moves energy between the network and one attached context, after every [ToEnergyPath] block along the route has taken its cut.
     * Because the cut is taken before the move, what the other side accepts is scaled back up by the same ratio to decide how much left the network at the far end.
     *
     * @param amount The amount of energy still to move.
     * @param extract Whether the energy is being pulled off the network rather than pushed onto it.
     * @param other The context at the far end of the route.
     * @param paths The blocks the energy travels through on the way there.
     * @param transaction The transaction the movement is recorded in.
     * @return The amount of energy still left to move after this attempt.
     * @since 0.6.0
     */
    private fun move(amount: Long, extract: Boolean, other: ToEnergyTransferContext, paths: Iterable<BlockPos>, transaction: TransferTransaction): Long {
        val request = paths.fold(amount) { acc, path ->
            val state = network.states[path]
            (network.states[path]?.block as? ToEnergyPath)?.changeEnergy(acc, level, state!!, path, extract) ?: acc
        }

        val handled = (if (extract) other::extract else other::insert).invoke(Unit, request, transaction)
        val ratio = handled.toDouble() / request.toDouble()
        val result = (amount * ratio).toLong().coerceAtMost(amount)

        if (result > 0) {
            transaction.commit()
            return amount - result
        }
        return amount
    }

    /**
     * One face of one node on the network, which is where energy enters or leaves it.
     *
     * @property pos The position of the node.
     * @property side The face of the node the network is attached to.
     * @since 0.6.0
     */
    data class NodeSide(val pos: BlockPos, val side: Direction)

}
