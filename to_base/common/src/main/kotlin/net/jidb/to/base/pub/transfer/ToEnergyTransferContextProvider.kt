package net.jidb.to.base.pub.transfer

import net.jidb.to.base.api.transfer.TransferContextProvider
import net.jidb.to.base.api.transfer.item.ItemTransferContext
import net.jidb.to.base.pub.transfer.energy.ToEnergyItemProvider
import net.jidb.to.base.pub.transfer.energy.ToEnergyWorldlyProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object ToEnergyTransferContextProvider : TransferContextProvider<ToEnergyTransferContext> {

    override fun fromBlock(level: Level, pos: BlockPos, side: Direction?): ToEnergyTransferContext? {
        return ToEnergyWorldlyProvider.getTransferContext(level, pos, side)
    }

    override fun fromItemStack(stack: ItemStack, context: ItemTransferContext, slot: Int): ToEnergyTransferContext? {
        return ToEnergyItemProvider.getTransferContext(stack)
    }

}