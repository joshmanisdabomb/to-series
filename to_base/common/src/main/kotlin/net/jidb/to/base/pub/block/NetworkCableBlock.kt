package net.jidb.to.base.pub.block

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.block.network.BlockNetworkPositionType
import net.jidb.to.base.api.block.network.BlockNetworkType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ScheduledTickAccess
import net.minecraft.world.level.block.state.BlockState

abstract class NetworkCableBlock<N : BlockNetworkType>(properties: Properties) : CableBlock(properties) {

    abstract val network: N

    override fun shouldConnect(level: LevelReader, state: BlockState, pos: BlockPos, direction: Direction, state2: BlockState, pos2: BlockPos): CableAttachType {
        return when (network.getPositionType(level, pos2, state2, direction.opposite)) {
            BlockNetworkPositionType.NODE -> CableAttachType.NODE
            BlockNetworkPositionType.PATH -> CableAttachType.CABLE
            else -> CableAttachType.NONE
        }
    }

    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
        super.onPlace(state, level, pos, oldState, movedByPiston)
        if (!level.isClientSide && level is ServerLevel) {
            level.dataStorage.computeIfAbsent(ToBaseMod.savedData.block_networks).notifyWithNeighbors(network, pos)
        }
    }

    override fun updateShape(state: BlockState, level: LevelReader, ticks: ScheduledTickAccess, pos: BlockPos, directionToNeighbour: Direction, neighbourPos: BlockPos, neighbourState: BlockState, random: RandomSource): BlockState {
        val ret = super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random)
        if (ret.block == this && state != ret && !level.isClientSide && level is ServerLevel) {
            level.dataStorage.computeIfAbsent(ToBaseMod.savedData.block_networks).notify(network, pos, neighbourPos)
        }
        return ret
    }

    override fun destroy(level: LevelAccessor, pos: BlockPos, state: BlockState) {
        super.destroy(level, pos, state)
        if (!level.isClientSide && level is ServerLevel) {
            level.dataStorage.computeIfAbsent(ToBaseMod.savedData.block_networks).notifyWithNeighbors(network, pos)
        }
    }

}
