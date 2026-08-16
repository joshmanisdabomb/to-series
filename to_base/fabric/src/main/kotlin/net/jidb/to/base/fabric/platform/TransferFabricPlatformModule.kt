package net.jidb.to.base.fabric.platform

import net.jidb.to.base.api.platform.TransferPlatformModule
import net.jidb.to.base.fabric.transfer.FabricTransferTransaction
import net.jidb.to.base.fabric.transfer.energy.FabricEnergyTransferContextProvider
import net.jidb.to.base.fabric.transfer.item.FabricItemTransferContextProvider
import net.jidb.to.base.pub.transfer.energy.platform.EmptyPlatformEnergyTransferContextProvider
import net.jidb.to.base.service.Services

object TransferFabricPlatformModule : TransferPlatformModule() {

    override fun startRootTransaction() = FabricTransferTransaction()

    override val itemProvider = FabricItemTransferContextProvider
    override val energyProvider = if (Services.environment.isModLoaded("tech_reborn_energy")) FabricEnergyTransferContextProvider else EmptyPlatformEnergyTransferContextProvider

}
