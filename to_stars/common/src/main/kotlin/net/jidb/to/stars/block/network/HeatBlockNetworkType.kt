package net.jidb.to.stars.block.network

import net.jidb.to.base.api.block.network.BlockNetwork
import net.jidb.to.base.api.block.network.BlockNetworkPositionType
import net.jidb.to.base.api.block.network.BlockNetworkType
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.HeatCableBlock
import net.jidb.to.stars.block.HeatGeneratorBlock
import net.jidb.to.stars.block.entity.HeatGeneratorBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class HeatBlockNetworkType : BlockNetworkType() {

    override fun getPositionType(level: LevelReader, pos: BlockPos, state: BlockState, from: Direction?): BlockNetworkPositionType? {
        if (from == Direction.UP && state.block is HeatGeneratorBlock) return BlockNetworkPositionType.NODE
        if (from?.axis != Direction.Axis.Y && (state.`is`(ToStarsMod.blocks.boiler) || state.`is`(Blocks.WATER_CAULDRON))) return BlockNetworkPositionType.NODE
        if (state.block is HeatCableBlock) return BlockNetworkPositionType.PATH
        return null
    }

    override fun tick(level: ServerLevel, network: BlockNetwork) {
        val generators = network.states.filter { (pos, state) -> state.block is HeatGeneratorBlock }
        val total = generators.filter { (pos, state) -> level.isLoaded(pos) }.map { (pos, state) -> (level.getBlockEntity(pos) as? HeatGeneratorBlockEntity)?.heat ?: 0f }.sum()
        val cauldrons = network.states.filter { (pos, state) -> state.`is`(ToStarsMod.blocks.boiler) || state.`is`(Blocks.WATER_CAULDRON) }

        val active = (total / 100f).toInt().coerceAtMost(cauldrons.size)
        val share = if (active > 0) total / active else 0f

        cauldrons.onEachIndexed { index, (pos, state) ->
            if (!level.isLoaded(pos)) return@onEachIndexed
            val connections = network.connections[pos]?.filter { it.axis.isHorizontal } ?: return@onEachIndexed
            val heat = if (index < active) share else 0f
            for (direction in connections) {
                ToStarsMod.blocks.boiler.setHeat(level, state, pos, heat / connections.count(), direction)
            }
        }
    }

}
