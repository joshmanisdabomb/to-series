package net.jidb.to.base.pub.transfer.energy

import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext

/**
 * A [ToEnergyTransferContext] that holds no energy and accepts none, the opposite of [InfiniteToEnergyTransferContext].
 * It is what to return where a context is required but the thing behind it has nothing to offer, which saves every caller from handling a null.
 *
 * @since 0.8.0
 */
object EmptyToEnergyTransferContext : ToEnergyTransferContext {

    override fun getSlotCount() = 1

    override fun getAmountAt(index: Int) = 0L

    override fun getCapacityAt(resource: Unit, index: Int) = 0L

    override fun insert(resource: Unit, amount: Long, transaction: TransferTransaction) = 0L

    override fun extract(resource: Unit, amount: Long, transaction: TransferTransaction) = 0L

}
