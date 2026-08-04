package net.jidb.to.base.neoforge.transfer

import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.TransferTransactionJournal
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.Transaction

/**
 * [TransferTransaction] implementation for Neoforge, which wraps one of its transfer API's own transactions.
 *
 * @param parent The transaction this one is nested inside, or `null` to open an outermost one. Defaults to `null`.
 * @since 0.6.0
 */
class ForgeTransferTransaction(parent: Transaction? = null) : TransferTransaction {

    /**
     * The Neoforge transaction this one is carried out through.
     *
     * @since 0.6.0
     */
    val transaction = if (parent != null) Transaction.open(parent) else Transaction.openRoot()

    override fun openNested() = ForgeTransferTransaction(transaction)

    override fun commit() = transaction.commit()

    override fun <S : Any> update(journal: TransferTransactionJournal<S>) {
        val journal = object : SnapshotJournal<S>() {

            override fun createSnapshot() = journal.create()

            override fun revertToSnapshot(snapshot: S) = journal.rewind(snapshot)

            override fun onRootCommit(originalState: S) = journal.onFinalCommit()

        }
        journal.updateSnapshots(transaction)
    }

    override fun close() = transaction.close()

}
