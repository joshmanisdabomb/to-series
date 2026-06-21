package net.jidb.to.base.api.transfer

interface TransferContext<R> {

    fun getSlotCount(): Int

    fun getContents() = (0 until getSlotCount()).map {
        val resource = getResourceAt(it)
        val amount = getAmountAt(resource, it)
        TransferContextContents(resource, amount, it)
    }

    fun getNonEmptyContents() = getContents().filter { it.amount > 0 }

    fun getGroupedContents() = getNonEmptyContents()
        .groupBy({ it.resource }, { it.amount })
        .mapValues { (_, amounts) -> amounts.sum() }

    fun getTotalAmount(resource: R): Long = (0 until getSlotCount()).sumOf { getAmountAt(resource, it) }

    fun getResourceAt(index: Int): R

    fun getAmountAt(resource: R, index: Int): Long

    fun getTotalCapacity(resource: R): Long = (0 until getSlotCount()).sumOf { getCapacityAt(resource, it) }

    fun getCapacityAt(resource: R, index: Int): Long

    fun insert(resource: R, amount: Long, transaction: TransferTransaction): Long

    fun extract(resource: R, amount: Long, transaction: TransferTransaction): Long

    data class TransferContextContents<R>(val resource: R, val amount: Long, val slot: Int)

    companion object {
        fun <R> moveAny(resource: R, amount: Long, from: TransferContext<R>, to: TransferContext<R>, transaction: TransferTransaction? = null): Long {
            (transaction?.openNested() ?: TransferTransaction.startRoot()).use {
                var simulated = 0L
                it.openNested().use {
                    simulated = to.insert(resource, amount, it)
                }

                if (simulated <= 0L) {
                    return@use
                }

                val extracted = from.extract(resource, simulated, it)
                if (extracted > 0) {
                    val inserted = to.insert(resource, extracted, it)
                    if (inserted == extracted) {
                        it.commit()
                        return inserted
                    }
                }
            }
            return 0L
        }

        fun <R> moveExact(resource: R, amount: Long, from: TransferContext<R>, to: TransferContext<R>, transaction: TransferTransaction? = null): Boolean {
            (transaction?.openNested() ?: TransferTransaction.startRoot()).use {
                val inserted = to.insert(resource, amount, it)
                if (inserted != amount) {
                    val extracted = from.extract(resource, inserted, it)
                    if (extracted == inserted) {
                        it.commit()
                        return true
                    }
                }
            }
            return false
        }
    }

}