package net.jidb.to.base.api.transfer.energy

import net.jidb.to.base.api.transfer.TransferContext

/**
 * Represents an abstract context for handling generic energy transfer operations.
 * Subclasses include [net.jidb.to.base.pub.transfer.ToEnergyTransferContext] for To Energy and [PlatformEnergyTransferContext].
 *
 * [Unit] being the resource means that there are no different instances of resource, only a single number representing "Energy".
 *
 * @since 0.6.0
 */
interface AbstractEnergyTransferContext : TransferContext<Unit> {

    override fun getResourceAt(index: Int) = Unit

    override fun getContents() = listOf(TransferContext.TransferContextContents(Unit, getTotalAmount(Unit), 0))

    override fun getGroupedContents() = mapOf(Unit to getTotalAmount(Unit))

}
