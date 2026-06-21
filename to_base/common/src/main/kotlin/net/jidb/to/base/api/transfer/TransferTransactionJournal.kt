package net.jidb.to.base.api.transfer

abstract class TransferTransactionJournal<S : Any>() {

    abstract fun create(): S

    abstract fun rewind(snapshot: S)

    open fun onFinalCommit() = Unit

    fun store(transaction: TransferTransaction) {
        transaction.update(this)
    }

}
