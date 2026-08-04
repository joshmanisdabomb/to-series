package net.jidb.to.base.pub.transfer

import net.jidb.to.base.api.transfer.TransferContextProvider
import net.jidb.to.base.api.transfer.item.ItemTransferContext
import net.jidb.to.base.pub.transfer.energy.ToEnergyItemProvider
import net.jidb.to.base.pub.transfer.energy.ToEnergyWorldlyProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * The [TransferContextProvider] that finds the To Energy held by a block or an item stack.
 * Both cases are answered by asking the thing itself: a block or block entity implementing [ToEnergyWorldlyProvider], and an item or one of its components implementing [ToEnergyItemProvider].
 *
 * Usually accessed through the transfer provider library:
 * ```kotlin
 * ToBaseMod.transferProviders.to_energy
 * ```
 *
 * @since 0.6.0
 */
object ToEnergyTransferContextProvider : TransferContextProvider<ToEnergyTransferContext> {

    override fun fromBlock(level: Level, pos: BlockPos, side: Direction?) = ToEnergyWorldlyProvider.getTransferContext(level, pos, side)

    override fun fromItemStack(stack: ItemStack, context: ItemTransferContext, slot: Int) = ToEnergyItemProvider.getTransferContext(stack)

}
