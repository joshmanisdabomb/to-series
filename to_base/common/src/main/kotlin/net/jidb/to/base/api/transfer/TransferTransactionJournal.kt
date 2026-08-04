package net.jidb.to.base.api.transfer

/**
 * A journal for tracking the state of transfer transactions. It allows creating snapshots of the current state and rewinding to a previous snapshot.
 * It is up to you to encapsulate a journal instance in your [TransferContext] and keep its state updated with [store].
 *
 * @param S The snapshot state type of this journal.
 * @since 0.7.0
 */
abstract class TransferTransactionJournal<S : Any> {

    /**
     * Creates a new snapshot of the current state.
     *
     * @return A new instance representing the snapshot state of this journal.
     * @since 0.7.0
     */
    abstract fun create(): S

    /**
     * Rewinds the journal to a previous snapshot state.
     *
     * @param snapshot The snapshot to rewind to. It represents the specific state to restore in the journal.
     * @since 0.7.0
     */
    abstract fun rewind(snapshot: S)

    /**
     * Hook method that is invoked when a final commit operation is performed on the journal.
     * Subclasses can override this method to execute additional actions or cleanup tasks during the final commit phase.
     *
     * @since 0.7.0
     */
    open fun onFinalCommit() = Unit

    /**
     * Stores the current state of the provided transaction in this journal.
     * This invokes the [update] method on the given transaction to ensure the journal's state reflects
     * the changes made in the transaction, enabling tracking and potential rollback functionality.
     *
     * @param transaction The transaction whose state is to be stored. It must implement the [TransferTransaction] interface.
     * @since 0.7.0
     */
    fun store(transaction: TransferTransaction) {
        transaction.update(this)
    }

}
