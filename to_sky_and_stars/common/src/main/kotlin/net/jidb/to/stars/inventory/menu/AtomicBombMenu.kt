package net.jidb.to.stars.inventory.menu

import net.jidb.to.stars.ToStarsMod
import net.minecraft.resources.Identifier
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import kotlin.math.min

class AtomicBombMenu(id: Int, playerInventory: Inventory, internal val container: Container) : AbstractContainerMenu(ToStarsMod.menus.atomic_bomb, id) {

    constructor(id: Int, playerInventory: Inventory) : this(id, playerInventory, SimpleContainer(allSlots.size))

    init {
        checkContainerSize(container, allSlots.size)

        container.startOpen(playerInventory.player)

        for (i in allSlots.indices) {
            addSlot(AtomicBombSlot(container, i, 50 + i.times(30), 22))
        }

        addStandardInventorySlots(playerInventory, 8, 89)
    }

    fun getErroredSlots(): IntArray {
        val set = mutableSetOf<Int>()
        for (index in 0 until container.containerSize) {
            val stack = container.getItem(index)
            if (!canPlaceItem(index, stack)) {
                set.add(index)
            }
        }
        return set.toIntArray()
    }

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        var result = ItemStack.EMPTY
        val slot = this.slots[index]

        if (slot.hasItem()) {
            val stackInSlot = slot.item
            result = stackInSlot.copy()

            if (index < allSlots.size) {
                if (!this.moveItemStackTo(stackInSlot, allSlots.size, slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.moveItemStackTo(stackInSlot, 0, allSlots.size, false)) {
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

    inner class AtomicBombSlot(container: Container, index: Int, x: Int, y: Int) : Slot(container, index, x, y) {

        override fun mayPlace(stack: ItemStack): Boolean {
            return canPlaceItem(index, stack)
        }

        override fun getMaxStackSize(stack: ItemStack): Int {
            return Companion.getMaxStackSize(stack)
        }

        override fun getNoItemIcon() = when (index) {
            in explosiveSlots -> Identifier.fromNamespaceAndPath(ToStarsMod.modid, "slot/tnt")
            in bulletSlots -> Identifier.fromNamespaceAndPath(ToStarsMod.modid, "slot/uranium_nugget")
            else -> Identifier.fromNamespaceAndPath(ToStarsMod.modid, "slot/uranium")
        }

        override fun setChanged() {
            super.setChanged()
            this@AtomicBombMenu.slotsChanged(container)
        }

    }

    companion object {
        val explosiveSlots = intArrayOf(0)
        val bulletSlots = intArrayOf(1)
        val fuelSlots = intArrayOf(2)
        val allSlots = intArrayOf(*explosiveSlots, *bulletSlots, *fuelSlots)

        fun getMaxStackSize(stack: ItemStack) = when (stack.item) {
            ToStarsMod.blocks.enriched_uranium_block.asItem() -> min(5, stack.maxStackSize)
            ToStarsMod.items.enriched_uranium.asItem() -> min(45, stack.maxStackSize)
            else -> 1
        }

        fun canPlaceItem(slot: Int, stack: ItemStack) = when (slot) {
            in explosiveSlots -> stack.`is`(Items.TNT)
            in bulletSlots -> stack.`is`(ToStarsMod.items.enriched_uranium_nugget)
            else -> stack.`is`(ToStarsMod.blocks.enriched_uranium_block.asItem()) || stack.`is`(ToStarsMod.items.enriched_uranium)
        }

    }

}
