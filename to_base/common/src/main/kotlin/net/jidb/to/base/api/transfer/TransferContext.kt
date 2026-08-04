package net.jidb.to.base.api.transfer

/**
 * A cross-platform transfer API interface that can support Neoforge's `ResourceHandler` and Fabric's `Storage`.
 *
 * You can see this object as some way to interact with a multi-slotted inventory of [R]s.
 * For example a chest would be `TransferContext<ItemResource>` with 27 slots, and an energy storage would be `TransferContext<Unit>` with 1 slot.
 *
 * @param R The type of resource being handled in the transfer context.
 * @since 0.6.0
 */
interface TransferContext<R> {

    /**
     * Retrieves the total number of slots available in the transfer context.
     *
     * @return the number of slots as an integer.
     * @since 0.6.0
     */
    fun getSlotCount(): Int

    /**
     * Retrieves a [List] of all slot contents within the transfer context.
     *
     * The method iterates over all available slots and retrieves the resource and amounts present at the corresponding slot.
     *
     * @return A [List] of [TransferContextContents] instances, each describing the resource, amount, and slot index.
     * @since 0.6.0
     */
    fun getContents() = (0 until getSlotCount()).map {
        val resource = getResourceAt(it)
        val amount = getAmountAt(it)
        TransferContextContents(resource, amount, it)
    }

    /**
     * Retrieves a [List] of non-empty contents from the transfer context.
     *
     * By default, filters out elements in [getContents] where [TransferContextContents.amount] is less than one.
     *
     * @return A [List] of [TransferContextContents] instances, where [TransferContextContents.amount] is greater than zero.
     * @since 0.6.0
     */
    fun getNonEmptyContents() = getContents().filter { it.amount > 0 }

    /**
     * Groups the contents of this transfer context based on the resource.
     *
     * This method groups [getNonEmptyContents] by their associated resource, with each resource serving as a key and the summed amounts as the value.
     *
     * @return A map of resources in the transfer context, and the total amounts of each.
     * @since 0.6.0
     */
    fun getGroupedContents() = getNonEmptyContents()
        .groupBy({ it.resource }, { it.amount })
        .mapValues { (_, amounts) -> amounts.sum() }

    /**
     * Calculates the total amount of the specified resource across all slots.
     *
     * @param resource The resource for which the total amount is to be calculated.
     * @return The total amount of the specified resource across all slots.
     * @since 0.6.0
     */
    fun getTotalAmount(resource: R): Long = (0 until getSlotCount()).sumOf { getAmountAt(it) }

    /**
     * Retrieves the resource located at the specified slot index within the context.
     *
     * @param index The index of the resource to retrieve.
     * @return The resource of type [R] present at the specified index.
     * @since 0.6.0
     */
    fun getResourceAt(index: Int): R

    /**
     * Retrieves the amount of a specified resource at a given slot index.
     *
     * @param index The index specifying the location of the resource.
     * @return The amount of the resource at the given index.
     * @since 0.6.0
     */
    fun getAmountAt(index: Int): Long

    /**
     * Calculates the total capacity of a given resource across all slots.
     *
     * @param resource The resource for which the total capacity is calculated.
     * @return The total capacity of the resource across all slots as a [Long] value.
     * @since 0.6.0
     */
    fun getTotalCapacity(resource: R): Long = (0 until getSlotCount()).sumOf { getCapacityAt(resource, it) }

    /**
     * Retrieves the maximum capacity available for a specific resource at the given slot index.
     *
     * The capacity should be given irrespective of the current amount or resource currently at that index, as if the slot was empty.
     *
     * @param resource The resource for which the capacity is being queried.
     * @param index The slot index to check for capacity.
     * @return The capacity available for the specified resource at the given slot.
     * @since 0.6.0
     */
    fun getCapacityAt(resource: R, index: Int): Long

    /**
     * Attempts to insert a specified amount of a resource into the context using the given transaction.
     *
     * @param resource The resource to be inserted.
     * @param amount The amount of the resource to insert.
     * @param transaction The transaction used to track and potentially reverse the insertion.
     * @return The amount of the resource that was actually inserted.
     * @since 0.6.0
     */
    fun insert(resource: R, amount: Long, transaction: TransferTransaction): Long

    /**
     * Attempts to extract a specified amount of a resource out of the context using the given transaction.
     *
     * @param resource The resource type to be extracted.
     * @param amount The amount of the resource to extract.
     * @param transaction The transaction used to track and potentially reverse the extraction.
     * @return The amount of the resource that was actually extracted.
     * @since 0.6.0
     */
    fun extract(resource: R, amount: Long, transaction: TransferTransaction): Long

    /**
     * Represents the contents of a specific slot in a transfer context.
     * It holds resource data, the amount stored, and the slot index.
     *
     * @param R The type of the resource being transferred.
     * @property resource The resource associated with this content.
     * @property amount The quantity of the resource in the specific slot.
     * @property slot The index of the slot this content belongs to.
     * @since 0.6.0
     */
    data class TransferContextContents<R>(val resource: R, val amount: Long, val slot: Int)

    companion object {

        /**
         * Transfers a specified amount of a given resource from one transfer context to another within a single nested or root transaction.
         *
         * @param R The type of the resource being transferred.
         * @param resource The resource to be moved between contexts.
         * @param amount The amount of the resource to transfer.
         * @param from The source transfer context from which the resource is extracted.
         * @param to The destination transfer context into which the resource is inserted.
         * @param transaction An optional transaction to birth a new nested transaction from, or `null` to start a new root transaction.
         * @return The actual amount of the resource successfully transferred.
         * @since 0.6.0
         */
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

        /**
         * Attempts to move an exact amount of a resource from one transfer context to another.
         * No changes are committed if the transfer cannot be fully completed.
         *
         * @param R The type of the resource being transferred.
         * @param resource The resource to be moved between contexts.
         * @param amount The amount of the resource to transfer.
         * @param from The source transfer context from which the resource is extracted.
         * @param to The destination transfer context into which the resource is inserted.
         * @param transaction An optional transaction to birth a new nested transaction from, or `null` to start a new root transaction.
         * @return Boolean if the total amount was successfully moved or not.
         * @since 0.6.0
         */
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
