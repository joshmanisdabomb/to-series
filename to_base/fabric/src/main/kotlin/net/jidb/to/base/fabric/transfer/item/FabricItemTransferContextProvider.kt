package net.jidb.to.base.fabric.transfer.item

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage
import net.jidb.to.base.api.transfer.item.ItemTransferContext
import net.jidb.to.base.api.transfer.item.ItemTransferContextProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * [ItemTransferContextProvider] implementation for Fabric, finding the items a block, a container or an item holds through the Transfer API's own lookups.
 *
 * @since 0.6.0
 */
object FabricItemTransferContextProvider : ItemTransferContextProvider() {

    override fun fromBlock(level: Level, pos: BlockPos, side: Direction?): ItemTransferContext? {
        return FabricItemTransferContext(ItemStorage.SIDED.find(level, pos, side) ?: return null)
    }

    override fun fromItemStack(stack: ItemStack, context: ItemTransferContext, slot: Int): ItemTransferContext? {
        val fabric = context as? FabricItemTransferContext ?: return null
        val slotted = fabric.storage as? SlottedStorage<ItemVariant> ?: return null
        return FabricItemTransferContext(ItemStorage.ITEM.find(stack, ContainerItemContext.ofSingleSlot(slotted.getSlot(slot))) ?: return null)
    }

    override fun fromContainer(container: Container, side: Direction?) = FabricItemTransferContext(ContainerStorage.of(container, side))

}
