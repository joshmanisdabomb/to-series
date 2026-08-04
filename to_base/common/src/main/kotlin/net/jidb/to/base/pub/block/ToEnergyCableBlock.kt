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

/**
 * The power cable, a [NetworkCableBlock] carrying To Energy over a [ToEnergyBlockNetworkType] network.
 * Asking the cable for its energy hands back the whole network it belongs to, so a machine pushing into any cable reaches everything the network is attached to.
 *
 * It also implements [ToEnergyPath], though the default takes no cut, so a subclass can make a cable that loses energy over distance without touching anything else.
 *
 * @param properties The vanilla block properties.
 * @property cableWidth How thick the cable itself is, in sixteenths of a block. Defaults to `4`.
 * @property rimWidth How far the rim around a node connection stands out from the cable, in sixteenths of a block. Defaults to `0`.
 * @since 0.6.0
 */
open class ToEnergyCableBlock(properties: Properties, override val cableWidth: Int = 4, override val rimWidth: Int = 0) : NetworkCableBlock<ToEnergyBlockNetworkType>(properties), ToEnergyWorldlyProvider, ToEnergyPath {

    override val network by lazy { ToBaseMod.blockNetworks.to_energy }

    /**
     * The transfer context built for each network and node this cable has been asked about, so that repeated lookups do not build a new one every time.
     *
     * @since 0.6.0
     */
    protected val contexts = mutableMapOf<Pair<UUID, NetworkToEnergyTransferContext.NodeSide?>, ToEnergyTransferContext>()

    /**
     * Retrieves the context for the network this cable is on, entered at the given side where that side leads to a node.
     *
     * @param level The level the cable is in.
     * @param pos The position of the cable.
     * @param side The face the energy is being reached through, or `null` where it is being read from no particular side.
     * @return The [ToEnergyTransferContext] for the network, or `null` where the cable is on no network of this type.
     * @since 0.6.0
     */
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
