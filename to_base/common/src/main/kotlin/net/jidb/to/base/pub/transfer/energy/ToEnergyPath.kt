package net.jidb.to.base.pub.transfer.energy

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

interface ToEnergyPath {

    fun changeEnergy(amount: Long, level: Level, state: BlockState, pos: BlockPos, extract: Boolean) = amount

}
