package net.jidb.to.base.pub.transfer.energy

import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext

/**
 * A [ToEnergyTransferContext] that wraps another and only lets energy out, which is how a side of a machine is made output-only.
 * Everything else is read straight from the wrapped context, so the energy shown is the machine's own.
 *
 * @property context The context this one wraps.
 * @see InputToEnergyTransferContext
 * @since 0.6.0
 */
class OutputToEnergyTransferContext(val context: ToEnergyTransferContext) : ToEnergyTransferContext {

    override fun getSlotCount() = context.getSlotCount()

    override fun getAmountAt(index: Int) = context.getAmountAt(index)

    override fun getCapacityAt(resource: Unit, index: Int) = context.getCapacityAt(resource, index)

    override fun insert(resource: Unit, amount: Long, transaction: TransferTransaction) = 0L

    override fun extract(resource: Unit, amount: Long, transaction: TransferTransaction) = context.extract(resource, amount, transaction)

}
