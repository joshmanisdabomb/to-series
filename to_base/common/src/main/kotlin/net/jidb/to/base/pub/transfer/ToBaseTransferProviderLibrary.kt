package net.jidb.to.base.pub.transfer

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.api.transfer.TransferContextProvider
import net.jidb.to.base.service.Services

/**
 * [SimpleLibrary] implementation that declares the transfer providers To Lay the Foundations offers, and provides access to them in one place.
 * A provider is what turns a block or item stack into a [net.jidb.to.base.api.transfer.TransferContext] for a kind of resource, so this is the entry point for reading or moving anything transferable.
 *
 * @since 0.6.0
 */
object ToBaseTransferProviderLibrary : SimpleLibrary<TransferContextProvider<*>>(ToBaseMod.modid) {

    /**
     * Finds the To Energy held by a block or item stack.
     *
     * @since 0.6.0
     */
    val to_energy by this { ToEnergyTransferContextProvider }

    /**
     * Finds the items held by a block or item stack, through whichever item transfer API the current modloader provides.
     *
     * @since 0.8.0
     */
    val items by this { Services.platform.transfer.itemProvider }

    /**
     * Finds the energy held by a block or item stack in the modloader's own energy unit, which is separate from To Energy.
     *
     * @since 0.8.0
     */
    val platform_energy by this { Services.platform.transfer.energyProvider }

}
