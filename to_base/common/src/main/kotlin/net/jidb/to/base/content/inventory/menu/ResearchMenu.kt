package net.jidb.to.base.content.inventory.menu

import net.jidb.to.base.content.ToBaseBlockLibrary
import net.jidb.to.base.content.ToBaseMenuLibrary
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack


class ResearchMenu(id: Int, playerInventory: Inventory, val access: ContainerLevelAccess) : AbstractContainerMenu(ToBaseMenuLibrary.research, id) {

    val player = playerInventory.player

    var active = true

    constructor(id: Int, playerInventory: Inventory) : this(id, playerInventory, ContainerLevelAccess.NULL)

    init {
        addStandardInventorySlots(playerInventory, 35, 137)
    }

    override fun addSlot(slot: Slot): Slot {
        return super.addSlot(ResearchSlot(slot.container, slot.containerSlot, slot.x, slot.y))
    }

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        var result = ItemStack.EMPTY
        val slot = this.slots.get(index)

        if (slot.hasItem()) {
            val stackInSlot = slot.item
            result = stackInSlot.copy()

            if (index in 0..<27) {
                // Main inventory -> hotbar
                if (!this.moveItemStackTo(stackInSlot, 27, 36, false)) {
                    return ItemStack.EMPTY
                }
            } else if (index in 27..<36) {
                // Hotbar -> main inventory
                if (!this.moveItemStackTo(stackInSlot, 0, 27, false)) {
                    return ItemStack.EMPTY
                }
            } else {
                // Ignore non-player slots
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

    override fun stillValid(player: Player) = stillValid(this.access, player, ToBaseBlockLibrary.research_desk)

    inner class ResearchSlot(container: Container, index: Int, x: Int, y: Int) : Slot(container, index, x, y) {

        override fun isActive() = active

        override fun isHighlightable() = active

    }

}