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

/**
 * [TransferContextProvider] implementation for Neoforge, finding the energy a block, an entity or an item holds through its own capabilities.
 *
 * @since 0.6.0
 */
object ForgeEnergyTransferContextProvider : TransferContextProvider<PlatformEnergyTransferContext> {

    override fun fromBlock(level: Level, pos: BlockPos, side: Direction?): PlatformEnergyTransferContext? {
        return ForgeEnergyTransferContext(level.getCapability(Capabilities.Energy.BLOCK, pos, side) ?: return null)
    }

    /**
     * The energy an entity holds, which Fabric has no equivalent of and so is not part of the common interface.
     *
     * @param entity The entity being asked about.
     * @param side The side the energy is being reached from, or `null` where it is not being reached from any particular one.
     * @return The context, or `null` where the entity holds no energy.
     * @since 0.6.0
     */
    fun fromEntity(entity: Entity, side: Direction?): PlatformEnergyTransferContext? {
        return ForgeEnergyTransferContext(entity.getCapability(Capabilities.Energy.ENTITY, side) ?: return null)
    }

    override fun fromItemStack(stack: ItemStack, context: ItemTransferContext, slot: Int): PlatformEnergyTransferContext? {
        val forge = context as? ForgeItemTransferContext ?: return null
        return ForgeEnergyTransferContext(ItemAccess.forHandlerIndex(forge.handler, slot).getCapability(Capabilities.Energy.ITEM) ?: return null)
    }

}
