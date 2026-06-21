package net.jidb.to.base.pub.transfer.energy

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.block.network.BlockNetwork
import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level

class NetworkToEnergyTransferContext(val network: BlockNetwork, val level: Level, val node: NodeSide? = null) : ToEnergyTransferContext {

    override fun getSlotCount() = 1

    override fun getAmountAt(resource: Unit, index: Int) = 0L

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

    data class NodeSide(val pos: BlockPos, val side: Direction)

}
