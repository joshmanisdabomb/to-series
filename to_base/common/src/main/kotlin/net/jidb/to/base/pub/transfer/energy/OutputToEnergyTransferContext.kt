package net.jidb.to.base.pub.transfer.energy

import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext

class OutputToEnergyTransferContext(val context: ToEnergyTransferContext) : ToEnergyTransferContext {

    override fun getSlotCount() = context.getSlotCount()

    override fun getAmountAt(resource: Unit, index: Int) = context.getAmountAt(resource, index)

    override fun getCapacityAt(resource: Unit, index: Int) = context.getCapacityAt(resource, index)

    override fun insert(resource: Unit, amount: Long, transaction: TransferTransaction) = 0L

    override fun extract(resource: Unit, amount: Long, transaction: TransferTransaction) = context.extract(resource, amount, transaction)

}
