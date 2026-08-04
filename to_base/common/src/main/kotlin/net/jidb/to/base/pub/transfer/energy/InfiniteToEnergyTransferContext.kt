package net.jidb.to.base.pub.transfer.energy

import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext

/**
 * A [ToEnergyTransferContext] that hands out as much energy as is ever asked of it and accepts none back, the opposite of [EmptyToEnergyTransferContext].
 * This is a creative energy source rather than a real one, so nothing it gives away is ever recorded in a transaction.
 *
 * @since 0.6.0
 */
object InfiniteToEnergyTransferContext : ToEnergyTransferContext {

    override fun getSlotCount() = 1

    override fun getAmountAt(index: Int) = Long.MAX_VALUE

    override fun getCapacityAt(resource: Unit, index: Int) = Long.MAX_VALUE

    override fun insert(resource: Unit, amount: Long, transaction: TransferTransaction) = 0L

    override fun extract(resource: Unit, amount: Long, transaction: TransferTransaction) = amount

}
