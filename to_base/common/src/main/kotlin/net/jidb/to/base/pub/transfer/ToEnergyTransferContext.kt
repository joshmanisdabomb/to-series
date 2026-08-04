package net.jidb.to.base.pub.transfer

import net.jidb.to.base.api.transfer.energy.AbstractEnergyTransferContext

/**
 * A [net.jidb.to.base.api.transfer.TransferContext] holding To Energy, the energy unit the To Series uses.
 * It adds nothing to [AbstractEnergyTransferContext]; the interface exists so that To Energy and a modloader's own energy are two different types that cannot be moved between by accident.
 *
 * @see net.jidb.to.base.api.transfer.energy.PlatformEnergyTransferContext
 * @since 0.6.0
 */
interface ToEnergyTransferContext : AbstractEnergyTransferContext
