package net.jidb.to.base.pub.transfer.energy

import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext

object InfiniteToEnergyTransferContext : ToEnergyTransferContext {

    override fun getSlotCount() = 1

    override fun getAmountAt(resource: Unit, index: Int) = Long.MAX_VALUE

    override fun getCapacityAt(resource: Unit, index: Int): Long = Long.MAX_VALUE

    override fun insert(resource: Unit, amount: Long, transaction: TransferTransaction) = 0L

    override fun extract(resource: Unit, amount: Long, transaction: TransferTransaction): Long = amount

}
