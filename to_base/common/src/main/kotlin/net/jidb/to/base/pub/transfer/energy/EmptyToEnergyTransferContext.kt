package net.jidb.to.base.pub.transfer.energy

import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext

object EmptyToEnergyTransferContext : ToEnergyTransferContext {

    override fun getSlotCount() = 1

    override fun getAmountAt(index: Int) = 0L

    override fun getCapacityAt(resource: Unit, index: Int) = 0L

    override fun insert(resource: Unit, amount: Long, transaction: TransferTransaction) = 0L

    override fun extract(resource: Unit, amount: Long, transaction: TransferTransaction) = 0L

}
