package net.jidb.to.base.api.transfer.energy

/**
 * Represents a cross-platform context for transferring energy.
 *
 * Fabric implements this with FabricEnergyTransferContext, which uses Tech Reborn £nergy API (optional mod to install).
 * Forge implements with ForgeEnergyTransferContext, using Neoforge's own energy API.
 *
 * @since 0.6.0
 */
interface PlatformEnergyTransferContext : AbstractEnergyTransferContext
