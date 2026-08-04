package net.jidb.to.base.neoforge.transfer.item

import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.item.ItemResource
import net.jidb.to.base.api.transfer.item.ItemTransferContext
import net.jidb.to.base.neoforge.transfer.ForgeTransferTransaction
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource as ForgeItemResource

/**
 * [ItemTransferContext] implementation for Neoforge, which wraps one of its resource handlers.
 *
 * Neoforge counts items in an `Int` rather than a `Long`, so an amount asked for is clamped to what one can hold before it is passed on.
 *
 * @property handler The resource handler the items are moved through.
 * @since 0.6.0
 */
class ForgeItemTransferContext(val handler: ResourceHandler<ForgeItemResource>) : ItemTransferContext {

    override fun getSlotCount() = handler.size()

    override fun getResourceAt(index: Int): ItemResource {
        val resource = handler.getResource(index)
        return ItemResource(resource.item, resource.componentsPatch)
    }

    override fun getAmountAt(index: Int) = handler.getAmountAsLong(index)

    override fun getCapacityAt(resource: ItemResource, index: Int) = handler.getCapacityAsLong(index, ForgeItemResource.of(resource.item, resource.components))

    override fun insert(resource: ItemResource, amount: Long, transaction: TransferTransaction) = handler.insert(ForgeItemResource.of(resource.item, resource.components), Math.clamp(amount, Int.MIN_VALUE, Int.MAX_VALUE), (transaction as ForgeTransferTransaction).transaction).toLong()

    override fun extract(resource: ItemResource, amount: Long, transaction: TransferTransaction) = handler.extract(ForgeItemResource.of(resource.item, resource.components), Math.clamp(amount, Int.MIN_VALUE, Int.MAX_VALUE), (transaction as ForgeTransferTransaction).transaction).toLong()

}
