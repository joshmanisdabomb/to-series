package net.jidb.to.base.api.transfer

import net.jidb.to.base.service.Services
import java.io.Closeable

/**
 * A cross-platform interface wrapping around Neoforge and Fabric's concepts of transfer transactions.
 * Transactions can be nested, and closed to reverse multistep changes.
 *
 * A class implementing [TransferContext] is likely to want to encapsulate a [TransferTransactionJournal] instance to keep track of changes in a [TransferTransaction] and store them for reverting functionality.
 *
 * @since 0.7.0
 */
interface TransferTransaction : Closeable {

    /**
     * Opens a nested transaction within the current transaction.
     * Nested transactions allow for grouping changes that can be individually committed or reverted without affecting the parent transaction.
     *
     * @return a new instance of [TransferTransaction] representing the nested transaction.
     * @since 0.7.0
     */
    fun openNested(): TransferTransaction

    /**
     * Finalizes the current transaction, applying all changes made within it.
     * Once a root transaction is committed, the changes cannot be reverted.
     * If the transaction is nested, it commits only the changes made within this nested transaction to the parent transaction.
     *
     * @since 0.7.0
     */
    fun commit()

    /**
     * Updates the state of the current transaction on the given [TransferTransactionJournal].
     * The journal provides a mechanism to track changes made during the transaction as snapshots. These snapshots can then be reverted.
     *
     * @param S  The type of object used to represent the journal entry.
     * @param journal A [TransferTransactionJournal] instance to track changes into, usually belongs to your [TransferContext].
     * @since 0.7.0
     */
    fun <S : Any> update(journal: TransferTransactionJournal<S>)

    companion object {

        /**
         * Creates a new root-level [TransferTransaction].
         * Once a root transsaction is committed, its changes cannot be reverted.
         *
         * @return A root-level [TransferTransaction] instance for managing transfer operations.
         * @since 0.7.0
         */
        fun startRoot() = Services.platform.transfer.startRootTransaction()

    }

}
