package net.jidb.to.base.fabric.transfer.energy

import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.energy.PlatformEnergyTransferContext
import net.jidb.to.base.fabric.transfer.FabricTransferTransaction
import team.reborn.energy.api.EnergyStorage

/**
 * [PlatformEnergyTransferContext] implementation for Fabric, which wraps one of Team Reborn's energy storages.
 *
 * Energy is not divided into slots, so the whole of it is reported as one.
 *
 * @property handler The energy storage the energy is moved through.
 * @since 0.6.0
 */
class FabricEnergyTransferContext(val handler: EnergyStorage) : PlatformEnergyTransferContext {

    override fun getSlotCount() = 1

    override fun getTotalAmount(resource: Unit) = handler.amount

    override fun getAmountAt(index: Int) = handler.amount

    override fun getTotalCapacity(resource: Unit) = handler.capacity

    override fun getCapacityAt(resource: Unit, index: Int): Long = handler.capacity

    override fun insert(resource: Unit, amount: Long, transaction: TransferTransaction) = handler.insert(amount, (transaction as FabricTransferTransaction).transaction)

    override fun extract(resource: Unit, amount: Long, transaction: TransferTransaction) = handler.extract(amount, (transaction as FabricTransferTransaction).transaction)

}
