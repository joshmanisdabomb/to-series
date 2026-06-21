package net.jidb.to.base.neoforge.transfer.energy

import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.energy.PlatformEnergyTransferContext
import net.jidb.to.base.neoforge.transfer.ForgeTransferTransaction
import net.neoforged.neoforge.transfer.energy.EnergyHandler

class ForgeEnergyTransferContext(val handler: EnergyHandler) : PlatformEnergyTransferContext {

    override fun getSlotCount() = 1

    override fun getTotalAmount(resource: Unit) = handler.amountAsLong

    override fun getAmountAt(resource: Unit, index: Int) = handler.amountAsLong

    override fun getTotalCapacity(resource: Unit) = handler.capacityAsLong

    override fun getCapacityAt(resource: Unit, index: Int): Long = handler.capacityAsLong

    override fun insert(resource: Unit, amount: Long, transaction: TransferTransaction): Long {
        return handler.insert(Math.clamp(amount, Int.MIN_VALUE, Int.MAX_VALUE), (transaction as ForgeTransferTransaction).transaction).toLong()
    }

    override fun extract(resource: Unit, amount: Long, transaction: TransferTransaction): Long {
        return handler.extract(Math.clamp(amount, Int.MIN_VALUE, Int.MAX_VALUE), (transaction as ForgeTransferTransaction).transaction).toLong()
    }

}
