package net.jidb.to.stars.inventory.menu

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.inventory.ContainerDataSchema
import net.jidb.to.base.pub.inventory.slot.EnergyItemSlot
import net.jidb.to.stars.ToStarsMod
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack

class EnergyStorageMenu(id: Int, playerInventory: Inventory, internal val container: Container, internal val data: ContainerData) : AbstractContainerMenu(ToStarsMod.menus.energy_storage, id) {

    constructor(id: Int, playerInventory: Inventory) : this(id, playerInventory, SimpleContainer(allSlots.size), SimpleContainerData(dataSchema.getDataSize()))

    init {
        checkContainerSize(container, allSlots.size)

        container.startOpen(playerInventory.player)

        addSlot(EnergyItemSlot(container, 0, 69, 36, ::slotsChanged))
        addSlot(EnergyItemSlot(container, 1, 91, 36, ::slotsChanged))

        addStandardInventorySlots(playerInventory, 8, 83)

        checkContainerDataCount(data, dataSchema.getDataSize())

        addDataSlots(data)
    }

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        var result = ItemStack.EMPTY
        val slot = this.slots[index]

        if (slot.hasItem()) {
            val stackInSlot = slot.item
            val energy = stackInSlot.get(ToBaseMod.itemComponents.energy_data)
            result = stackInSlot.copy()

            if (index in allSlots) {
                if (!this.moveItemStackTo(stackInSlot, allSlots.size, slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (energy != null && energy.energy <= 0L) {
                if (!this.moveItemStackTo(stackInSlot, transferToSlots.min(), transferToSlots.max() + 1, false)) {
                    return ItemStack.EMPTY
                }
            } else if (energy != null && energy.energy >= energy.max) {
                if (!this.moveItemStackTo(stackInSlot, transferFromSlots.min(), transferFromSlots.max() + 1, false)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.moveItemStackTo(stackInSlot, allSlots.min(), allSlots.max() + 1, false)) {
                return ItemStack.EMPTY
            }

            if (stackInSlot.isEmpty) {
                slot.setByPlayer(ItemStack.EMPTY)
            } else {
                slot.setChanged()
            }

            if (stackInSlot.count == result.count) {
                return ItemStack.EMPTY
            }

            slot.onTake(player, stackInSlot)
        }

        return result
    }

    override fun stillValid(player: Player) = container.stillValid(player)

    companion object {
        val transferFromSlots = intArrayOf(0)
        val transferToSlots = intArrayOf(1)
        val allSlots = intArrayOf(*transferFromSlots, *transferToSlots)

        val dataSchema = ContainerDataSchema<EnergyStorageDataKey>()
            .defineLong(EnergyStorageDataKey.TOTAL)
            .defineLong(EnergyStorageDataKey.CAPACITY)
            .defineLong(EnergyStorageDataKey.MAX_INPUT)
            .defineLong(EnergyStorageDataKey.MAX_OUTPUT)
            .defineLong(EnergyStorageDataKey.INSERT_CHANGE)
            .defineLong(EnergyStorageDataKey.INSERT_AVERAGE)
            .defineLong(EnergyStorageDataKey.EXTRACT_CHANGE)
            .defineLong(EnergyStorageDataKey.EXTRACT_AVERAGE)
    }

    enum class EnergyStorageDataKey {
        TOTAL,
        CAPACITY,
        MAX_INPUT,
        MAX_OUTPUT,
        INSERT_CHANGE,
        INSERT_AVERAGE,
        EXTRACT_CHANGE,
        EXTRACT_AVERAGE,
    }

}
