package net.jidb.to.base.neoforge.transfer.energy

import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.energy.PlatformEnergyTransferContext
import net.jidb.to.base.neoforge.transfer.ForgeTransferTransaction
import net.neoforged.neoforge.transfer.energy.EnergyHandler

/**
 * [PlatformEnergyTransferContext] implementation for Neoforge, which wraps one of its energy handlers.
 *
 * Energy is not divided into slots, so the whole of it is reported as one.
 * Neoforge counts energy in an `Int` rather than a `Long`, so an amount asked for is clamped to what one can hold before it is passed on.
 *
 * @property handler The energy handler the energy is moved through.
 * @since 0.6.0
 */
class ForgeEnergyTransferContext(val handler: EnergyHandler) : PlatformEnergyTransferContext {

    override fun getSlotCount() = 1

    override fun getTotalAmount(resource: Unit) = handler.amountAsLong

    override fun getAmountAt(index: Int) = handler.amountAsLong

    override fun getTotalCapacity(resource: Unit) = handler.capacityAsLong

    override fun getCapacityAt(resource: Unit, index: Int): Long = handler.capacityAsLong

    override fun insert(resource: Unit, amount: Long, transaction: TransferTransaction) = handler.insert(Math.clamp(amount, Int.MIN_VALUE, Int.MAX_VALUE), (transaction as ForgeTransferTransaction).transaction).toLong()

    override fun extract(resource: Unit, amount: Long, transaction: TransferTransaction) = handler.extract(Math.clamp(amount, Int.MIN_VALUE, Int.MAX_VALUE), (transaction as ForgeTransferTransaction).transaction).toLong()

}
