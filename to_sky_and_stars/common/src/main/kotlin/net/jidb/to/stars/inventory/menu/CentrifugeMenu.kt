package net.jidb.to.stars.inventory.menu

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.inventory.slot.CraftingOutputSlot
import net.jidb.to.base.pub.inventory.slot.ToEnergyItemSlot
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class CentrifugeMenu(id: Int, playerInventory: Inventory, container: Container, data: ContainerData) : ProcessorMenu(ToStarsMod.menus.centrifuge, id, playerInventory, container, data) {

    override val inputSlots = Companion.inputSlots
    override val outputSlots = Companion.outputSlots
    override val batterySlots = Companion.batterySlots

    override var clientData: ProcessorMenuData? = null

    constructor(id: Int, playerInventory: Inventory, data: ProcessorMenuData) : this(id, playerInventory, SimpleContainer(allSlots.size), SimpleContainerData(dataSchema.getDataSize())) {
        clientData = data
    }

    init {
        checkContainerSize(container, allSlots.size)

        container.startOpen(playerInventory.player)

        addSlot(Slot(container, 0, 80, 46))
        addSlot(CraftingOutputSlot(container, playerInventory.player, inputSlots, 1, 41, 77))
        addSlot(CraftingOutputSlot(container, playerInventory.player, inputSlots, 2, 119, 77))
        addSlot(ToEnergyItemSlot(container, 3, 8, 18, ::slotsChanged))

        addStandardInventorySlots(playerInventory, 8, 127)

        checkContainerDataCount(data, dataSchema.getDataSize())

        addDataSlots(data)
    }

    override fun stillValid(player: Player) = container.stillValid(player)

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        var result = ItemStack.EMPTY
        val slot = this.slots[index]

        if (slot.hasItem()) {
            val stackInSlot = slot.item
            val itemTransfer = Services.platform.transfer.itemProvider.fromContainer(slot.container, null)
            val energy = if (itemTransfer != null) ToBaseMod.transferProviders.to_energy.fromItemStack(stackInSlot, itemTransfer, index) else null
            result = stackInSlot.copy()

            if (index in allSlots) {
                if (!this.moveItemStackTo(stackInSlot, allSlots.size, slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (energy != null) {
                if (!this.moveItemStackTo(stackInSlot, batterySlots.min(), batterySlots.max() + 1, false)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.moveItemStackTo(stackInSlot, inputSlots.min(), inputSlots.max() + 1, false)) {
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

    companion object {
        val inputSlots = intArrayOf(0)
        val outputSlots = intArrayOf(1, 2)
        val batterySlots = intArrayOf(3)
        val allSlots = intArrayOf(*inputSlots, *outputSlots, *batterySlots)
    }
    
}
