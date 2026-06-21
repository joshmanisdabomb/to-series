package net.jidb.to.base.api.transfer.energy

import net.jidb.to.base.api.transfer.TransferContext

interface AbstractEnergyTransferContext : TransferContext<Unit> {

    override fun getResourceAt(index: Int) = Unit

    override fun getContents() = listOf(TransferContext.TransferContextContents(Unit, getTotalAmount(Unit), 0))

    override fun getGroupedContents() = mapOf(Unit to getTotalAmount(Unit))

}
