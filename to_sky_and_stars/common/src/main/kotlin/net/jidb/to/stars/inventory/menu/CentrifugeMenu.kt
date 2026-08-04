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

/**
 * The interface a centrifuge is opened into, which has one input, two outputs and a slot for a battery to run it from.
 *
 * @param id The id of the menu.
 * @param playerInventory The inventory of the player who opened it.
 * @param container The centrifuge the menu is opened on.
 * @param data The centrifuge's figures, as the interface reads them.
 */
class CentrifugeMenu(id: Int, playerInventory: Inventory, container: Container, data: ContainerData) : ProcessorMenu(ToStarsMod.menus.centrifuge, id, playerInventory, container, data) {

    override val inputSlots = Companion.inputSlots
    override val outputSlots = Companion.outputSlots
    override val batterySlots = Companion.batterySlots

    override var clientData: ProcessorMenuData? = null

    /**
     * Creates the menu on the client, where the centrifuge itself is not available and what it needs to draw is sent with the opening packet instead.
     *
     * @param id The id of the menu.
     * @param playerInventory The inventory of the player who opened it.
     * @param data What the client needs to know about the centrifuge.
     */
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

        /**
         * The slot a recipe's ingredients are taken from.
         */
        val inputSlots = intArrayOf(0)

        /**
         * The slots a recipe's results are put into, which are not interchangeable.
         */
        val outputSlots = intArrayOf(1, 2)

        /**
         * The slot the centrifuge draws energy out of a battery in.
         */
        val batterySlots = intArrayOf(3)

        /**
         * Every slot of the centrifuge, in the order they are drawn.
         */
        val allSlots = intArrayOf(*inputSlots, *outputSlots, *batterySlots)

    }

}
