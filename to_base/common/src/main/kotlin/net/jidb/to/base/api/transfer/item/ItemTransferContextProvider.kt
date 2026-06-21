package net.jidb.to.base.api.transfer.item

import net.jidb.to.base.api.transfer.TransferContextProvider
import net.minecraft.core.Direction
import net.minecraft.world.Container

abstract class ItemTransferContextProvider : TransferContextProvider<ItemTransferContext> {

    abstract fun fromContainer(container: Container, side: Direction?): ItemTransferContext?

}
