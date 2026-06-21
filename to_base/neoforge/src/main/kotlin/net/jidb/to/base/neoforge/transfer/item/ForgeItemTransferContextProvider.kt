package net.jidb.to.base.neoforge.transfer.item

import net.jidb.to.base.api.transfer.item.ItemTransferContext
import net.jidb.to.base.api.transfer.item.ItemTransferContextProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.Container
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper

object ForgeItemTransferContextProvider : ItemTransferContextProvider() {

    override fun fromBlock(level: Level, pos: BlockPos, side: Direction?): ItemTransferContext? {
        return ForgeItemTransferContext(level.getCapability(Capabilities.Item.BLOCK, pos, side) ?: return null)
    }

    fun fromEntity(entity: Entity): ItemTransferContext? {
        return ForgeItemTransferContext(entity.getCapability(Capabilities.Item.ENTITY) ?: return null)
    }

    override fun fromItemStack(stack: ItemStack, context: ItemTransferContext, slot: Int): ItemTransferContext? {
        val forge = context as? ForgeItemTransferContext ?: return null
        return ForgeItemTransferContext(ItemAccess.forHandlerIndex(forge.handler, slot).getCapability(Capabilities.Item.ITEM) ?: return null)
    }

    override fun fromContainer(container: Container, side: Direction?): ItemTransferContext {
        if (container is WorldlyContainer) {
            return ForgeItemTransferContext(WorldlyContainerWrapper(container, side))
        }
        return ForgeItemTransferContext(VanillaContainerWrapper.of(container))
    }

}
