package net.jidb.to.base.pub.transfer

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.api.transfer.TransferContextProvider

object ToBaseTransferProviderLibrary : SimpleLibrary<TransferContextProvider<*>>(ToBaseMod.modid) {

    val to_energy by this { ToEnergyTransferContextProvider }

}
