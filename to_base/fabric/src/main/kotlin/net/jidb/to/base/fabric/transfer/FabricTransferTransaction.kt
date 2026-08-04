package net.jidb.to.base.fabric.transfer

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant
import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.TransferTransactionJournal

/**
 * [TransferTransaction] implementation for Fabric, which wraps one of the Transfer API's own transactions.
 *
 * @param parent The transaction this one is nested inside, or `null` to open an outermost one. Defaults to `null`.
 * @since 0.6.0
 */
class FabricTransferTransaction(parent: Transaction? = null) : TransferTransaction {

    /**
     * The Transfer API transaction this one is carried out through.
     *
     * @since 0.6.0
     */
    val transaction = if (parent != null) Transaction.openNested(parent) else Transaction.openOuter()

    override fun openNested() = FabricTransferTransaction(transaction)

    override fun commit() = transaction.commit()

    override fun <S : Any> update(journal: TransferTransactionJournal<S>) {
        val journal = object : SnapshotParticipant<S>() {

            override fun createSnapshot() = journal.create()

            override fun readSnapshot(snapshot: S) = journal.rewind(snapshot)

            override fun onFinalCommit() = journal.onFinalCommit()

        }
        journal.updateSnapshots(transaction)
    }

    override fun close() = transaction.close()

}
