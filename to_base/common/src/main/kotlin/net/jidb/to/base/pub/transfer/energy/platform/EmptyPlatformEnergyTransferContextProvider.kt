package net.jidb.to.base.pub.transfer.energy.platform

import net.jidb.to.base.api.transfer.TransferContextProvider
import net.jidb.to.base.api.transfer.energy.PlatformEnergyTransferContext
import net.jidb.to.base.api.transfer.item.ItemTransferContext
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object EmptyPlatformEnergyTransferContextProvider : TransferContextProvider<PlatformEnergyTransferContext> {

    override fun fromBlock(level: Level, pos: BlockPos, side: Direction?) = null

    override fun fromItemStack(stack: ItemStack, context: ItemTransferContext, slot: Int) = null

}
