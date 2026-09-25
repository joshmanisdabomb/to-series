package net.jidb.to.stars.block

import net.jidb.to.base.pub.block.ToEnergyCableBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

/**
 * A power cable that loses some of the energy passing through it, so that carrying energy a long way costs something.
 *
 * The loss is applied in whichever direction the energy is moving: what comes out is less than what went in, and what has to go in to give a wanted amount out is more.
 * Something is always let through, however lossy the cable, so that a trickle never rounds down to nothing at all.
 *
 * @property loss The fraction of the energy lost across the cable.
 * @param properties The block's own properties.
 * @property cableWidth How wide the cable is drawn, in pixels. Defaults to `4`.
 * @property rimWidth How wide the rim where it meets a machine is drawn, in pixels. Defaults to `0`.
 */
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
