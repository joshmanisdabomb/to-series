package net.jidb.to.base.fabric.transfer.item

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.item.ItemResource
import net.jidb.to.base.api.transfer.item.ItemTransferContext
import net.jidb.to.base.fabric.transfer.FabricTransferTransaction

/**
 * [ItemTransferContext] implementation for Fabric, which wraps one of the Transfer API's own item storages.
 *
 * A storage need not be divided into slots at all, in which case its contents are read as a list and indexed by position instead.
 *
 * @property storage The item storage the items are moved through.
 * @since 0.6.0
 */
class FabricItemTransferContext(val storage: Storage<ItemVariant>) : ItemTransferContext {

    override fun getSlotCount(): Int {
        if (storage is SlottedStorage) return storage.slotCount
        return storage.count()
    }

    override fun getResourceAt(index: Int): ItemResource {
        val resource = if (storage is SlottedStorage) {
            storage.getSlot(index).resource
        } else {
            storage.toList().getOrNull(index)?.resource ?: ItemVariant.blank()
        }
        return ItemResource(resource.item, resource.componentsPatch)
    }

    override fun getAmountAt(index: Int): Long {
        if (storage is SlottedStorage) return storage.getSlot(index).amount
        return storage.toList().getOrNull(index)?.amount ?: 0L
    }

    override fun getCapacityAt(resource: ItemResource, index: Int): Long {
        if (storage is SlottedStorage) return storage.getSlot(index).capacity
        return storage.toList().getOrNull(index)?.capacity ?: 0L
    }

    override fun insert(resource: ItemResource, amount: Long, transaction: TransferTransaction) = storage.insert(ItemVariant.of(resource.item, resource.components), amount, (transaction as FabricTransferTransaction).transaction)

    override fun extract(resource: ItemResource, amount: Long, transaction: TransferTransaction) = storage.extract(ItemVariant.of(resource.item, resource.components), amount, (transaction as FabricTransferTransaction).transaction)

}
