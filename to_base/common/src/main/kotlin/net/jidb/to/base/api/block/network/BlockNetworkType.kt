package net.jidb.to.base.api.block.network

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState

abstract class BlockNetworkType {

    abstract fun getPositionType(level: LevelReader, pos: BlockPos, state: BlockState, from: Direction? = null): BlockNetworkPositionType?

    abstract fun tick(level: ServerLevel, network: BlockNetwork)

}