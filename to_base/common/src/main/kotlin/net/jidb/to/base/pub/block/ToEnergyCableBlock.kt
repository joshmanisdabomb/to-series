package net.jidb.to.base.pub.block

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.block.network.energy.ToEnergyBlockNetworkType
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.jidb.to.base.pub.transfer.energy.NetworkToEnergyTransferContext
import net.jidb.to.base.pub.transfer.energy.ToEnergyPath
import net.jidb.to.base.pub.transfer.energy.ToEnergyWorldlyProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.LevelReader
import java.util.UUID

open class ToEnergyCableBlock(properties: Properties, override val cableWidth: Int = 4, override val rimWidth: Int = 0) : NetworkCableBlock<ToEnergyBlockNetworkType>(properties), ToEnergyWorldlyProvider, ToEnergyPath {

    override val network by lazy { ToBaseMod.blockNetworks.to_energy }

    protected val contexts = mutableMapOf<Pair<UUID, NetworkToEnergyTransferContext.NodeSide?>, ToEnergyTransferContext>()

    protected fun getNetworkContext(level: ServerLevel, pos: BlockPos, side: Direction?): ToEnergyTransferContext? {
        val network = level.dataStorage.computeIfAbsent(ToBaseMod.savedData.block_networks)[pos]?.firstOrNull { it.type == network } ?: return null
        if (side != null) {
            val nodePos = pos.relative(side)
            if (network.nodes.contains(nodePos)) {
                val node = NetworkToEnergyTransferContext.NodeSide(nodePos, side.opposite)
                return contexts.getOrPut(Pair(network.id, node)) { NetworkToEnergyTransferContext(network, level, node) }
            }
        }
        return contexts.getOrPut(Pair(network.id, null)) { NetworkToEnergyTransferContext(network, level, null) }
    }

    override fun getTransferContext(level: LevelReader, pos: BlockPos, side: Direction?): ToEnergyTransferContext? {
        if (level !is ServerLevel) return null
        val state = level.getBlockState(pos)
        if (state.block != this) return null
        if (side != null && state.getValue(attachments[side]!!) == CableAttachType.NONE) return null
        return getNetworkContext(level, pos, side)
    }

}
