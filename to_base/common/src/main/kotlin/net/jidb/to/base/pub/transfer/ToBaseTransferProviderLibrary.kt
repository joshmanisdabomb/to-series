package net.jidb.to.base.pub.transfer

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.api.transfer.TransferContextProvider
import net.jidb.to.base.service.Services

object ToBaseTransferProviderLibrary : SimpleLibrary<TransferContextProvider<*>>(ToBaseMod.modid) {

    val to_energy by this { ToEnergyTransferContextProvider }

    val items by this { Services.platform.transfer.itemProvider }
    val platform_energy by this { Services.platform.transfer.energyProvider }

}
