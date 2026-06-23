package net.jidb.to.stars.block.network

import net.jidb.to.base.api.block.network.BlockNetwork
import net.jidb.to.base.api.block.network.BlockNetworkPositionType
import net.jidb.to.base.api.block.network.BlockNetworkType
import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.stars.block.HeatCableBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState

class HeatBlockNetworkType : BlockNetworkType() {

    override fun getPositionType(level: LevelReader, pos: BlockPos, state: BlockState, from: Direction?): BlockNetworkPositionType? {
        if (from == Direction.UP && state.block.identifier == Identifier.fromNamespaceAndPath("to_sky_and_stars", "gold_machine_enclosure")) return BlockNetworkPositionType.NODE
        if (state.block is HeatCableBlock) return BlockNetworkPositionType.PATH
        return null
    }

    override fun tick(level: ServerLevel, network: BlockNetwork) {
        println("Heat network ${network.id} in ${level.dimension()}")
        println("Nodes: ${network.nodes}, Paths: ${network.paths}")
    }

}