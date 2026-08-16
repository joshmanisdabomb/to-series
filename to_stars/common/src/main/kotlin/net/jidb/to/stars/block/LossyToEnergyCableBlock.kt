package net.jidb.to.stars.block

import net.jidb.to.base.pub.block.ToEnergyCableBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class LossyToEnergyCableBlock(val loss: Float, properties: Properties, override val cableWidth: Int = 4, override val rimWidth: Int = 0) : ToEnergyCableBlock(properties, cableWidth, rimWidth) {

    override fun changeEnergy(amount: Long, level: Level, state: BlockState, pos: BlockPos, extract: Boolean): Long {
        if (amount <= 0L) return 0L
        val result = super.changeEnergy(amount, level, state, pos, extract)
        when (extract) {
            true -> return result.div(1f - loss).toLong()
            false -> return result.times(1f - loss).toLong().coerceAtLeast(1L)
        }
    }

}
