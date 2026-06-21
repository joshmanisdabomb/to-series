package net.jidb.to.base.api.block.network

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState

data class BlockNetworkPath(val path: List<BlockPos>, val states: List<BlockState>) {

    val from = path.first()
    val to = path.last()
    val paths = path.drop(1).dropLast(1)
    val outgoing = Direction.entries.firstOrNull { it.unitVec3i == (paths.firstOrNull() ?: to).subtract(from) }
    val incoming = Direction.entries.firstOrNull { it.unitVec3i == to.subtract(paths.lastOrNull() ?: from) }

}
