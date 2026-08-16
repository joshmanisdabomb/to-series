package net.jidb.to.base.fabric.transfer

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant
import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.TransferTransactionJournal

class FabricTransferTransaction(parent: Transaction? = null) : TransferTransaction {

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
