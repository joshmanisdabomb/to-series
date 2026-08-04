package net.jidb.to.base.fabric.transfer.energy

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage
import net.jidb.to.base.api.transfer.TransferContextProvider
import net.jidb.to.base.api.transfer.energy.PlatformEnergyTransferContext
import net.jidb.to.base.api.transfer.item.ItemTransferContext
import net.jidb.to.base.fabric.transfer.item.FabricItemTransferContext
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import team.reborn.energy.api.EnergyStorage

/**
 * [TransferContextProvider] implementation for Fabric, finding the energy a block or an item holds through Team Reborn's own lookups.
 *
 * @since 0.6.0
 */
object FabricEnergyTransferContextProvider : TransferContextProvider<PlatformEnergyTransferContext> {

    override fun fromBlock(level: Level, pos: BlockPos, side: Direction?): PlatformEnergyTransferContext? {
        return FabricEnergyTransferContext(EnergyStorage.SIDED.find(level, pos, side) ?: return null)
    }

    override fun fromItemStack(stack: ItemStack, context: ItemTransferContext, slot: Int): PlatformEnergyTransferContext? {
        val fabric = context as? FabricItemTransferContext ?: return null
        val slotted = fabric.storage as? SlottedStorage<ItemVariant> ?: return null
        return FabricEnergyTransferContext(EnergyStorage.ITEM.find(stack, ContainerItemContext.ofSingleSlot(slotted.getSlot(slot))) ?: return null)
    }

}
