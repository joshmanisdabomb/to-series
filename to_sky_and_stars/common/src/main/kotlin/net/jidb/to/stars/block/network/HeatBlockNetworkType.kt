package net.jidb.to.stars.block.network

import net.jidb.to.base.api.block.network.BlockNetwork
import net.jidb.to.base.api.block.network.BlockNetworkPositionType
import net.jidb.to.base.api.block.network.BlockNetworkType
import net.jidb.to.stars.block.HeatCableBlock
import net.jidb.to.stars.block.HeatGeneratorBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState

class HeatBlockNetworkType : BlockNetworkType() {

    override fun getPositionType(level: LevelReader, pos: BlockPos, state: BlockState, from: Direction?): BlockNetworkPositionType? {
        if (from == Direction.UP && state.block is HeatGeneratorBlock) return BlockNetworkPositionType.NODE
        if (state.block is HeatCableBlock) return BlockNetworkPositionType.PATH
        return null
    }

    override fun tick(level: ServerLevel, network: BlockNetwork) = Unit

}