package net.jidb.to.base.api.platform

import net.jidb.to.base.api.transfer.TransferContextProvider
import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.energy.PlatformEnergyTransferContext
import net.jidb.to.base.api.transfer.item.ItemTransferContextProvider

/**
 * A [Platform]-specific module that defines a cross-platform contract for using the transfer systems of the current modloader.
 * This module has code for creating a root [TransferTransaction] and implements for item and platform energy [TransferContextProvider]s.
 *
 * @since 0.6.0
 */
abstract class TransferPlatformModule {

    /**
     * Initiates and returns a new root-level transfer transaction.
     *
     * @return A new instance of cross-platform [TransferTransaction], representing the root transaction.
     * @since 0.6.0
     */
    abstract fun startRootTransaction(): TransferTransaction

    /**
     * Cross-platform implementation for providing [net.jidb.to.base.api.transfer.item.ItemTransferContext]s from side access in a [net.minecraft.world.level.Level], or from [net.minecraft.world.item.ItemStack]s.
     *
     * @since 0.6.0
     */
    abstract val itemProvider: ItemTransferContextProvider

    /**
     * Cross-platform implementation for providing [PlatformEnergyTransferContext]s from side access in a [net.minecraft.world.level.Level], or from [net.minecraft.world.item.ItemStack]s.
     *
     * @since 0.6.0
     */
    abstract val energyProvider: TransferContextProvider<PlatformEnergyTransferContext>

}
