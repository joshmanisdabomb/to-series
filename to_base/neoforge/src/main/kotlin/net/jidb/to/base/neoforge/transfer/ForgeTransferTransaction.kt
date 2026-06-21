package net.jidb.to.base.neoforge.transfer

import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.TransferTransactionJournal
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.Transaction

class ForgeTransferTransaction(parent: Transaction? = null) : TransferTransaction {

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