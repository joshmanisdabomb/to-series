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

/**
 * The menu opened by a research desk, through which the in-game wiki is read.
 * The desk holds nothing of its own, so the only slots are the player's own, and those can be hidden while the wiki is being read by clearing [active].
 *
 * @param id The menu's synchronisation ID.
 * @param playerInventory The inventory of the player who opened the menu.
 * @property access The level and position the menu was opened at, used to check the desk is still there.
 * @since 0.1.0
 */
class ResearchMenu(id: Int, playerInventory: Inventory, val access: ContainerLevelAccess) : AbstractContainerMenu(ToBaseMenuLibrary.research, id) {

    /**
     * The player the menu was opened by.
     *
     * @since 0.1.0
     */
    val player = playerInventory.player

    /**
     * Whether the player's slots are currently shown and clickable, which the wiki turns off while it is covering them.
     *
     * @since 0.1.0
     */
    var active = true

    /**
     * Creates the menu on the client, where the position it was opened at is not known.
     *
     * @param id The menu's synchronisation ID.
     * @param playerInventory The inventory of the player who opened the menu.
     * @since 0.1.0
     */
    constructor(id: Int, playerInventory: Inventory) : this(id, playerInventory, ContainerLevelAccess.NULL)

    init {
        addStandardInventorySlots(playerInventory, 35, 137)
    }

    override fun addSlot(slot: Slot) = super.addSlot(ResearchSlot(slot.container, slot.containerSlot, slot.x, slot.y))

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        var result = ItemStack.EMPTY
        val slot = this.slots[index]

        if (slot.hasItem()) {
            val stackInSlot = slot.item
            result = stackInSlot.copy()

            if (index in 0..<27) {
                //Main inventory -> hotbar
                if (!this.moveItemStackTo(stackInSlot, 27, 36, false)) {
                    return ItemStack.EMPTY
                }
            } else if (index in 27..<36) {
                //Hotbar -> main inventory
                if (!this.moveItemStackTo(stackInSlot, 0, 27, false)) {
                    return ItemStack.EMPTY
                }
            } else {
                //Ignore non-player slots
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

    /**
     * A player inventory slot that hides itself along with the rest of them when the outer menu is not [active].
     *
     * @param container The container the slot reads from.
     * @param index The index of the slot within the container.
     * @param x The x position of the slot in the screen.
     * @param y The y position of the slot in the screen.
     * @since 0.1.0
     */
    inner class ResearchSlot(container: Container, index: Int, x: Int, y: Int) : Slot(container, index, x, y) {

        override fun isActive() = active

        override fun isHighlightable() = active

    }

}
