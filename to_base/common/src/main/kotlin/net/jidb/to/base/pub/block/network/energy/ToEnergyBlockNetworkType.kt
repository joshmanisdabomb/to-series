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

class ToEnergyBlockNetworkType : BlockNetworkType() {

    override fun getPositionType(level: LevelReader, pos: BlockPos, state: BlockState, from: Direction?): BlockNetworkPositionType? {
        return if (state.block is ToEnergyCableBlock) BlockNetworkPositionType.PATH
        else if (ToEnergyWorldlyProvider.getTransferContext(level, pos, from) != null) BlockNetworkPositionType.NODE
        else null
    }

    override fun tick(level: ServerLevel, network: BlockNetwork) = Unit

}