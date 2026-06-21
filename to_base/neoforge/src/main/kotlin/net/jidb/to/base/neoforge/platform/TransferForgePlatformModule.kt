package net.jidb.to.base.neoforge.platform

import net.jidb.to.base.api.platform.TransferPlatformModule
import net.jidb.to.base.neoforge.transfer.ForgeTransferTransaction
import net.jidb.to.base.neoforge.transfer.energy.ForgeEnergyTransferContextProvider
import net.jidb.to.base.neoforge.transfer.item.ForgeItemTransferContextProvider

object TransferForgePlatformModule : TransferPlatformModule() {

    override fun startRootTransaction() = ForgeTransferTransaction()

    override val itemProvider = ForgeItemTransferContextProvider
    override val energyProvider = ForgeEnergyTransferContextProvider

}
