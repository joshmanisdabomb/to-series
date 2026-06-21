package net.jidb.to.base.neoforge.transfer.energy

import net.jidb.to.base.api.transfer.TransferContextProvider
import net.jidb.to.base.api.transfer.energy.PlatformEnergyTransferContext
import net.jidb.to.base.api.transfer.item.ItemTransferContext
import net.jidb.to.base.neoforge.transfer.item.ForgeItemTransferContext
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.transfer.access.ItemAccess

object ForgeEnergyTransferContextProvider : TransferContextProvider<PlatformEnergyTransferContext> {

    override fun fromBlock(level: Level, pos: BlockPos, side: Direction?): PlatformEnergyTransferContext? {
        return ForgeEnergyTransferContext(level.getCapability(Capabilities.Energy.BLOCK, pos, side) ?: return null)
    }

    fun fromEntity(entity: Entity, side: Direction?): PlatformEnergyTransferContext? {
        return ForgeEnergyTransferContext(entity.getCapability(Capabilities.Energy.ENTITY, side) ?: return null)
    }

    override fun fromItemStack(stack: ItemStack, context: ItemTransferContext, slot: Int): PlatformEnergyTransferContext? {
        val forge = context as? ForgeItemTransferContext ?: return null
        return ForgeEnergyTransferContext(ItemAccess.forHandlerIndex(forge.handler, slot).getCapability(Capabilities.Energy.ITEM) ?: return null)
    }

}
