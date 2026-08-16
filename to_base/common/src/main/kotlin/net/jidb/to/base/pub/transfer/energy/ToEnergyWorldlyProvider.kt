package net.jidb.to.base.pub.transfer.energy

import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.LevelReader

interface ToEnergyWorldlyProvider {

    fun getTransferContext(level: LevelReader, pos: BlockPos, side: Direction?): ToEnergyTransferContext?

    companion object {

        fun getProvider(level: LevelReader, pos: BlockPos): ToEnergyWorldlyProvider? {
            val entity = level.getBlockEntity(pos)
            if (entity is ToEnergyWorldlyProvider) {
                return entity
            }
            val block = level.getBlockState(pos).block
            if (block is ToEnergyWorldlyProvider) {
                return block
            }
            return null
        }

        fun getTransferContext(level: LevelReader, pos: BlockPos, side: Direction?) = getProvider(level, pos)?.getTransferContext(level, pos, side)

    }

}
