package net.jidb.to.base.pub.transfer.energy

import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.TransferTransactionJournal
import net.jidb.to.base.pub.block.entity.ToEnergyBlockEntityHandler
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import kotlin.math.max
import kotlin.math.min

class BlockEntityToEnergyTransferContext(val handler: ToEnergyBlockEntityHandler) : ToEnergyTransferContext {

    val journal = object : TransferTransactionJournal<LongArray>() {
        override fun create() = longArrayOf(handler.energy, handler.historyInsert[0], handler.historyExtract[0])
        override fun rewind(snapshot: LongArray) {
            handler.energy = snapshot[0]
            handler.historyInsert[0] = snapshot[1]
            handler.historyExtract[0] = snapshot[2]
        }
    }

    override fun getSlotCount() = 1

    override fun getAmountAt(resource: Unit, index: Int) = handler.energy

    override fun getCapacityAt(resource: Unit, index: Int) = handler.capacity

    override fun insert(resource: Unit, amount: Long, transaction: TransferTransaction): Long {
        val remaining = max(0L, handler.maxInput - handler.historyInsert[0])
        val inserted = min(handler.capacity - handler.energy, min(amount, remaining))
        if (inserted > 0) {
            journal.store(transaction)
            handler.energy += inserted
            handler.historyInsert[0] += inserted
            return inserted
        } else {
            return 0
        }
    }

    override fun extract(resource: Unit, amount: Long, transaction: TransferTransaction): Long {
        val remaining = max(0L, handler.maxOutput - handler.historyExtract[0])
        val extracted = min(handler.energy, min(amount, remaining))
        if (extracted > 0) {
            journal.store(transaction)
            handler.energy -= extracted
            handler.historyExtract[0] += extracted
            return extracted
        } else {
            return 0
        }
    }

}