package net.jidb.to.stars.block.entity

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.transfer.TransferContext
import net.jidb.to.base.pub.block.entity.ToEnergyBlockEntityHandler
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.jidb.to.base.pub.transfer.energy.InputToEnergyTransferContext
import net.jidb.to.base.pub.transfer.energy.OutputToEnergyTransferContext
import net.jidb.to.base.pub.transfer.energy.ToEnergyItemProvider
import net.jidb.to.base.pub.transfer.energy.ToEnergyWorldlyProvider
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.EnergyStorageBlock
import net.jidb.to.stars.inventory.menu.EnergyStorageMenu
import net.jidb.to.stars.network.EnergySyncPayload
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.ContainerHelper
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

class EnergyStorageBlockEntity(pos: BlockPos, state: BlockState) : BaseContainerBlockEntity(ToStarsMod.blockEntities.power_bank, pos, state), WorldlyContainer, ToEnergyWorldlyProvider {

    protected var inventory = NonNullList.withSize(EnergyStorageMenu.allSlots.size, ItemStack.EMPTY)

    val energy = ToEnergyBlockEntityHandler(
        (state.block as? EnergyStorageBlock)?.machine?.bankStorage ?: 0L,
        (state.block as? EnergyStorageBlock)?.machine?.maxInput ?: 0L,
        (state.block as? EnergyStorageBlock)?.machine?.maxOutput ?: 0L,
    )
    val transferInput = InputToEnergyTransferContext(energy.transfer)
    val transferOutput = OutputToEnergyTransferContext(energy.transfer)

    override fun getDefaultName() = blockState.block.name

    override fun getItems() = inventory

    public override fun setItems(items: NonNullList<ItemStack>) {
        inventory = items
    }

    override fun createMenu(id: Int, player: Inventory) = EnergyStorageMenu(id, player, this, energy.dataAccess)

    override fun getContainerSize() = inventory.size

    override fun getMaxStackSize() = 1

    override fun getMaxStackSize(stack: ItemStack) = 1

    override fun getSlotsForFace(side: Direction) = when (side) {
        Direction.UP -> EnergyStorageMenu.transferFromSlots
        Direction.DOWN -> EnergyStorageMenu.allSlots
        else -> EnergyStorageMenu.transferToSlots
    }

    override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
        val current = inventory[slot]
        if (current.count > getMaxStackSize()) {
            return false
        }

        val itemTransfer = ToBaseMod.transferProviders.items.fromContainer(this, null) ?: return false
        val energy = ToBaseMod.transferProviders.to_energy.fromItemStack(stack, itemTransfer, slot)
        return energy != null
    }

    override fun canPlaceItemThroughFace(slot: Int, stack: ItemStack, side: Direction?) = canPlaceItem(slot, stack)

    override fun canTakeItemThroughFace(slot: Int, stack: ItemStack, side: Direction): Boolean {
        val energy = ToEnergyItemProvider.getTransferContext(stack) ?: return false
        if (slot in EnergyStorageMenu.transferFromSlots) {
            return energy.getTotalAmount(Unit) == 0L
        } else if (slot in EnergyStorageMenu.transferToSlots) {
            return energy.getTotalAmount(Unit) == energy.getTotalCapacity(Unit)
        }
        return true
    }

    override fun loadAdditional(input: ValueInput) {
        super.loadAdditional(input)
        energy.loadAdditional(input)
        items.clear()
        ContainerHelper.loadAllItems(input, items)
    }

    override fun saveAdditional(output: ValueOutput) {
        energy.saveAdditional(output)
        ContainerHelper.saveAllItems(output, items)
        super.saveAdditional(output)
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        val tag = CompoundTag()
        energy.getUpdateTag(tag, registries)
        return tag
    }

    override fun getTransferContext(level: LevelReader, pos: BlockPos, side: Direction?): ToEnergyTransferContext {
        if (side == null) return energy.transfer
        val output = blockState.getValue(DirectionalBlock.FACING)
        if (side == output) return transferOutput
        return transferInput
    }

    override fun applyImplicitComponents(components: DataComponentGetter) {
        super.applyImplicitComponents(components)
        energy.applyImplicitComponents(components)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        energy.collectImplicitComponents(components)
    }

    companion object {
        fun tick(level: Level, pos: BlockPos, state: BlockState, entity: EnergyStorageBlockEntity) {
            if (level.isClientSide) return

            entity.energy.tickAverages()

            val itemTransfer = Services.platform.transfer.itemProvider.fromContainer(entity, null)
            if (itemTransfer != null) {
                for (i in EnergyStorageMenu.transferFromSlots) {
                    val item = ToBaseMod.transferProviders.to_energy.fromItemStack(entity.inventory[i], itemTransfer, i)
                    if (item != null) {
                        TransferContext.moveAny(Unit, entity.energy.maxInput, item, entity.energy.transfer)
                    }
                }
                for (i in EnergyStorageMenu.transferToSlots) {
                    val item = ToBaseMod.transferProviders.to_energy.fromItemStack(entity.inventory[i], itemTransfer, i)
                    if (item != null) {
                        TransferContext.moveAny(Unit, entity.energy.maxOutput, entity.energy.transfer, item)
                    }
                }
            }

            val facing = state.getValue(DirectionalBlock.FACING)
            if (level.isLoaded(pos.relative(facing))) {
                val other = ToBaseMod.transferProviders.to_energy.fromBlock(level, pos.relative(facing), facing.opposite)
                if (other != null) {
                    val context = entity.getTransferContext(level, pos, facing)
                    TransferContext.moveAny(Unit, entity.energy.maxOutput, context, other)
                }
            }

            if (entity.energy.shouldNetworkSync()) {
                val slevel = level as? ServerLevel
                if (slevel != null) {
                    Services.platform.networking.sendToPlayersTrackingChunk(slevel, ChunkPos.containing(pos), EnergySyncPayload(pos, entity.energy.energy))
                }
            }
        }
    }

}
