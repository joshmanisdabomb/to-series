package net.jidb.to.base.api.platform

import net.jidb.to.base.api.transfer.TransferContextProvider
import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.energy.PlatformEnergyTransferContext
import net.jidb.to.base.api.transfer.item.ItemTransferContextProvider

abstract class TransferPlatformModule {

    abstract fun startRootTransaction(): TransferTransaction

    abstract val itemProvider: ItemTransferContextProvider
    abstract val energyProvider: TransferContextProvider<PlatformEnergyTransferContext>

}
