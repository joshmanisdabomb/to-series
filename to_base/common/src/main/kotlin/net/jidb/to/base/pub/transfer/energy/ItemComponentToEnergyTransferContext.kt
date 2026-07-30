package net.jidb.to.base.pub.transfer.energy

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.KotlinHelper.either
import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.TransferTransactionJournal
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.minecraft.world.item.ItemStack
import kotlin.math.min

class ItemComponentToEnergyTransferContext(val stack: ItemStack) : ToEnergyTransferContext {

    protected var energy
        get() = stack.get(ToBaseMod.itemComponents.energy_data)?.energy ?: 0L
        set(value) { stack.set(ToBaseMod.itemComponents.energy_data, stack.get(ToBaseMod.itemComponents.energy_data)?.copy(energy = value) ?: return) }
    protected val capacity get() = stack.get(ToBaseMod.itemComponents.energy_data)?.max ?: 0L
    protected val maxInput get() = stack.get(ToBaseMod.itemComponents.energy_data)?.maxInput ?: Long.MAX_VALUE
    protected val maxOutput get() = stack.get(ToBaseMod.itemComponents.energy_data)?.maxOutput ?: Long.MAX_VALUE

    val journal = object : TransferTransactionJournal<Long>() {
        override fun create() = stack.get(ToBaseMod.itemComponents.energy_data)?.energy ?: 0L
        override fun rewind(snapshot: Long) {
            energy = snapshot
        }
    }

    override fun getSlotCount() = stack.has(ToBaseMod.itemComponents.energy_data).either(1, 0)

    override fun getAmountAt(resource: Unit, index: Int) = energy

    override fun getCapacityAt(resource: Unit, index: Int) = capacity

    override fun insert(resource: Unit, amount: Long, transaction: TransferTransaction): Long {
        val inserted = min(capacity - energy, min(amount, maxInput))
        if (inserted > 0) {
            journal.store(transaction)
            energy += inserted
            return inserted
        } else {
            return 0
        }
    }

    override fun extract(resource: Unit, amount: Long, transaction: TransferTransaction): Long {
        val extracted = min(energy, min(amount, maxOutput))
        if (extracted > 0) {
            journal.store(transaction)
            energy -= extracted
            return extracted
        } else {
            return 0
        }
    }

}