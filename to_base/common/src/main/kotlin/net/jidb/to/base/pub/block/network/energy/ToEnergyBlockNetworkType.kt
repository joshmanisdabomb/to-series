package net.jidb.to.base.pub.block.network.energy

import net.jidb.to.base.api.block.network.BlockNetwork
import net.jidb.to.base.api.block.network.BlockNetworkPositionType
import net.jidb.to.base.api.block.network.BlockNetworkType
import net.jidb.to.base.pub.block.ToEnergyCableBlock
import net.jidb.to.base.pub.transfer.energy.ToEnergyWorldlyProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState

/**
 * The [BlockNetworkType] for To Energy, whose paths are [ToEnergyCableBlock] and whose nodes are anything that holds energy.
 * The network moves nothing by itself: energy only travels when something pushes into or pulls out of it, so there is nothing to do on a tick.
 *
 * @since 0.6.0
 */
class ToEnergyBlockNetworkType : BlockNetworkType() {

    override fun getPositionType(level: LevelReader, pos: BlockPos, state: BlockState, from: Direction?): BlockNetworkPositionType? {
        if (state.block is ToEnergyCableBlock) {
            return BlockNetworkPositionType.PATH
        } else if (ToEnergyWorldlyProvider.getTransferContext(level, pos, from) != null) {
            return BlockNetworkPositionType.NODE
        }
        return null
    }

    override fun tick(level: ServerLevel, network: BlockNetwork) = Unit

}
