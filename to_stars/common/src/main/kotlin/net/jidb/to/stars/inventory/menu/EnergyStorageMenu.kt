package net.jidb.to.stars.inventory.menu

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.block.entity.ToEnergyBlockEntityHandler
import net.jidb.to.base.pub.inventory.ToBaseQuickStackHelper
import net.jidb.to.base.pub.inventory.slot.ToEnergyItemSlot
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData

/**
 * The interface a power bank is opened into, which has one slot for charging something and one for draining it.
 *
 * @param id The id of the menu.
 * @param playerInventory The inventory of the player who opened it.
 * @property container The power bank the menu is opened on.
 * @property data The power bank's figures, as the interface reads them.
 */
class EnergyStorageMenu(id: Int, playerInventory: Inventory, internal val container: Container, internal val data: ContainerData) : AbstractContainerMenu(ToStarsMod.menus.energy_storage, id) {

    /**
     * Creates the menu on the client, where the power bank itself is not available and an empty container stands in for it.
     *
     * @param id The id of the menu.
     * @param playerInventory The inventory of the player who opened it.
     */
    constructor(id: Int, playerInventory: Inventory) : this(id, playerInventory, SimpleContainer(allSlots.size), SimpleContainerData(ToEnergyBlockEntityHandler.dataSchema.getDataSize()))

    init {
        checkContainerSize(container, allSlots.size)

        container.startOpen(playerInventory.player)

        addSlot(ToEnergyItemSlot(container, 0, 69, 36, ::slotsChanged))
        addSlot(ToEnergyItemSlot(container, 1, 91, 36, ::slotsChanged))

        addStandardInventorySlots(playerInventory, 8, 83)

        checkContainerDataCount(data, ToEnergyBlockEntityHandler.dataSchema.getDataSize())

        addDataSlots(data)
    }

    override fun quickMoveStack(player: Player, index: Int) = ToBaseQuickStackHelper.start(this, player, index) { stack, index, slot, player ->
        val from = ToBaseQuickStackHelper.fromContainer(::moveItemStackTo, stack, index, slots.size)
        if (from != null) return@start from

        val itemTransfer = Services.platform.transfer.itemProvider.fromContainer(slot.container, null)
        val energy = if (itemTransfer != null) ToBaseMod.transferProviders.to_energy.fromItemStack(stack, itemTransfer, index) else null
        if (energy != null) {
            if (energy.getTotalAmount(Unit) <= 0L) {
                if (ToBaseQuickStackHelper.toContainer(::moveItemStackTo, stack, index, slots.size, transferToSlots.toList().toIntArray()) == true) {
                    return@start true
                }
            } else if (energy.getTotalAmount(Unit) >= energy.getTotalCapacity(Unit)) {
                if (ToBaseQuickStackHelper.toContainer(::moveItemStackTo, stack, index, slots.size, transferFromSlots.toList().toIntArray()) == true) {
                    return@start true
                }
            }
        } else if (ToBaseQuickStackHelper.toContainer(::moveItemStackTo, stack, index, slots.size, allSlots.toList().toIntArray()) == true) {
            return@start true
        }
        ToBaseQuickStackHelper.betweenInventoryHotbar(::moveItemStackTo, stack, index, slots.size) == true
    }

    override fun stillValid(player: Player) = container.stillValid(player)

    companion object {

        /**
         * The slot the power bank draws energy out of.
         */
        val transferFromSlots = intArrayOf(0)

        /**
         * The slot the power bank puts energy into.
         */
        val transferToSlots = intArrayOf(1)

        /**
         * Every slot of the power bank, in the order they are drawn.
         */
        val allSlots = intArrayOf(*transferFromSlots, *transferToSlots)

    }

}
