package net.jidb.to.base.api.transfer

import net.jidb.to.base.service.Services
import java.io.Closeable

interface TransferTransaction : Closeable {

    fun openNested(): TransferTransaction

    fun commit()

    fun <S : Any> update(journal: TransferTransactionJournal<S>)

    companion object {
        fun startRoot() = Services.platform.transfer.startRootTransaction()
    }

}