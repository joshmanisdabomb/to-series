package net.jidb.to.base.pub.inventory

import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

/**
 * Helper object to assist in writing stack moving code for [AbstractContainerMenu.quickMoveStack] functions.
 * Has functions to start a quick move to skip boilerplate, and functions to move between containers and hotbars and extended player inventory.
 *
 * @since 1.1.0
 */
object ToBaseQuickStackHelper {

    fun start(menu: AbstractContainerMenu, player: Player, index: Int, operations: (stack: ItemStack, index: Int, slot: Slot, player: Player) -> Boolean): ItemStack {
        var result = ItemStack.EMPTY
        val slot = menu.slots[index]

        if (slot.hasItem()) {
            val stackInSlot = slot.item
            result = stackInSlot.copy()

            if (!operations(stackInSlot, index, slot, player)) {
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

    fun toSlots(move: (stack: ItemStack, start: Int, end: Int, backwards: Boolean) -> Boolean, stack: ItemStack, index: Int, target: IntArray, backwards: Boolean = false): Boolean {
        val clone = target.copyOf()
        clone.sort()
        val ranges = clone.fold(mutableListOf<IntRange>()) { ranges, n ->
            val last = ranges.lastOrNull()

            if (last != null && n == last.last + 1) {
                ranges[ranges.lastIndex] = last.first..n
            } else {
                ranges.add(n..n)
            }

            ranges
        }

        for (range in ranges) {
            if (move(stack, range.first, range.last + 1, backwards)) return true
        }
        return false
    }

    fun standard(move: (stack: ItemStack, start: Int, end: Int, backwards: Boolean) -> Boolean, stack: ItemStack, index: Int, totalSlots: Int): Boolean {
        val from = fromContainer(move, stack, index, totalSlots)
        if (from != null) return from
        return toContainer(move, stack, index, totalSlots, null) == true && betweenInventoryHotbar(move, stack, index, totalSlots) == true
    }

    fun fromContainer(move: (stack: ItemStack, start: Int, end: Int, backwards: Boolean) -> Boolean, stack: ItemStack, index: Int, totalSlots: Int): Boolean? {
        if (index >= totalSlots - 36) return null
        return move(stack, totalSlots - 36, totalSlots, true)
    }

    fun toContainer(move: (stack: ItemStack, start: Int, end: Int, backwards: Boolean) -> Boolean, stack: ItemStack, index: Int, totalSlots: Int, target: IntArray? = null): Boolean? {
        if (index < totalSlots - 36) return null
        return toSlots(move, stack, index, target ?: (0 ..< totalSlots-36).toList().toIntArray(), false)
    }

    fun fromHotbar(move: (stack: ItemStack, start: Int, end: Int, backwards: Boolean) -> Boolean, stack: ItemStack, index: Int, totalSlots: Int): Boolean? {
        if (index < totalSlots - 9) return null
        return move(stack, totalSlots - 36, totalSlots - 9, false)
    }

    fun toHotbar(move: (stack: ItemStack, start: Int, end: Int, backwards: Boolean) -> Boolean, stack: ItemStack, index: Int, totalSlots: Int): Boolean? {
        if (index < totalSlots - 36 && index >= totalSlots - 9) return null
        return move(stack, totalSlots - 9, totalSlots, false)
    }

    fun betweenInventoryHotbar(move: (stack: ItemStack, start: Int, end: Int, backwards: Boolean) -> Boolean, stack: ItemStack, index: Int, totalSlots: Int): Boolean? {
        if (index < totalSlots - 36) return null
        if (fromHotbar(move, stack, index, totalSlots) == true) return true
        return toHotbar(move, stack, index, totalSlots) == true
    }

}
