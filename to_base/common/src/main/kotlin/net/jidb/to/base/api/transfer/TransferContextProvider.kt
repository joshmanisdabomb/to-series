package net.jidb.to.base.api.transfer

import net.jidb.to.base.api.transfer.item.ItemTransferContext
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

interface TransferContextProvider<C : TransferContext<*>> {

    fun fromBlock(level: Level, pos: BlockPos, side: Direction? = null): C?

    fun fromItemStack(stack: ItemStack, context: ItemTransferContext, slot: Int): C?

}
