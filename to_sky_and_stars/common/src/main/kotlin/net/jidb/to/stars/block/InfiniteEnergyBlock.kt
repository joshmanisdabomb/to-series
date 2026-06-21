package net.jidb.to.stars.block

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.pub.transfer.energy.InfiniteToEnergyTransferContext
import net.jidb.to.base.pub.transfer.energy.ToEnergyWorldlyProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class InfiniteEnergyBlock(properties: Properties) : Block(properties), ToEnergyWorldlyProvider {

    override fun getTransferContext(level: LevelReader, pos: BlockPos, side: Direction?) = InfiniteToEnergyTransferContext

    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        level.scheduleTick(pos, this, 1)
        super.onPlace(state, level, pos, oldState, notify)
    }

    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        TransferTransaction.startRoot().use {
            for (dir in Direction.entries) {
                val context = ToBaseMod.transferProviders.to_energy.fromBlock(level, pos.relative(dir), dir.opposite) ?: continue
                it.openNested().use {
                    val amount = context.insert(Unit, Long.MAX_VALUE, it)
                    if (amount > 0) {
                        it.commit()
                    }
                }
            }
            it.commit()
        }
        level.scheduleTick(pos, this, 1)
        super.tick(state, level, pos, random)
    }

}
