package net.jidb.to.base.pub.transfer.energy

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.KotlinHelper.either
import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.TransferTransactionJournal
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.minecraft.world.item.ItemStack
import kotlin.math.min

/**
 * The [ToEnergyTransferContext] for an item stack that carries a [net.jidb.to.base.pub.item.component.ToEnergyItemComponentData] component.
 * Everything it reports is read back out of that component each time, so the stack itself remains the only place the energy is stored.
 *
 * @property stack The item stack whose energy component is being read and written.
 * @since 0.8.0
 */
class ItemComponentToEnergyTransferContext(val stack: ItemStack) : ToEnergyTransferContext {

    /**
     * The energy currently stored on the stack, written back into a copy of the component when set.
     * Setting it on a stack with no component at all does nothing, rather than adding one.
     *
     * @since 0.8.0
     */
    private var energy
        get() = stack.get(ToBaseMod.itemComponents.energy_data)?.energy ?: 0L
        set(value) { stack.set(ToBaseMod.itemComponents.energy_data, stack.get(ToBaseMod.itemComponents.energy_data)?.copy(energy = value) ?: return) }

    /**
     * How much energy the stack can hold, or `0` where it carries no energy component.
     *
     * @since 0.8.0
     */
    private val capacity get() = stack.get(ToBaseMod.itemComponents.energy_data)?.max ?: 0L

    /**
     * How much energy the stack will accept in one insertion, or no limit where it carries no energy component.
     *
     * @since 0.8.0
     */
    private val maxInput get() = stack.get(ToBaseMod.itemComponents.energy_data)?.maxInput ?: Long.MAX_VALUE

    /**
     * How much energy the stack will give up in one extraction, or no limit where it carries no energy component.
     *
     * @since 0.8.0
     */
    private val maxOutput get() = stack.get(ToBaseMod.itemComponents.energy_data)?.maxOutput ?: Long.MAX_VALUE

    /**
     * The journal that snapshots the stored energy, so that a transaction which is closed without being committed puts it back.
     *
     * @since 0.8.0
     */
    val journal = object : TransferTransactionJournal<Long>() {

        override fun create() = stack.get(ToBaseMod.itemComponents.energy_data)?.energy ?: 0L
        override fun rewind(snapshot: Long) {
            energy = snapshot
        }

    }

    override fun getSlotCount() = stack.has(ToBaseMod.itemComponents.energy_data).either(1, 0)

    override fun getAmountAt(index: Int) = energy

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
