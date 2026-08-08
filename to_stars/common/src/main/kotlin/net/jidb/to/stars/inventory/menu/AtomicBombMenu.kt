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

/**
 * The interface an atomic bomb is loaded and armed through, which has three slots: the explosive that sets it off, the bullet that starts the reaction and the uranium that fuels it.
 *
 * @param id The id of the menu.
 * @param playerInventory The inventory of the player who opened it.
 * @property container The bomb the menu is opened on.
 */
class AtomicBombMenu(id: Int, playerInventory: Inventory, internal val container: Container) : AbstractContainerMenu(ToStarsMod.menus.atomic_bomb, id) {

    /**
     * Creates the menu on the client, where the bomb itself is not available and an empty container stands in for it.
     *
     * @param id The id of the menu.
     * @param playerInventory The inventory of the player who opened it.
     */
    constructor(id: Int, playerInventory: Inventory) : this(id, playerInventory, SimpleContainer(allSlots.size))

    init {
        checkContainerSize(container, allSlots.size)

        container.startOpen(playerInventory.player)

        for (i in allSlots.indices) {
            addSlot(AtomicBombSlot(container, i, 50 + i.times(30), 22))
        }

        addStandardInventorySlots(playerInventory, 8, 89)
    }

    /**
     * Which slots hold something that does not belong in them, so that the screen can mark them and refuse to arm the bomb.
     *
     * @return The slots holding the wrong thing.
     */
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

    /**
     * One of the bomb's three slots, which only accepts what belongs in it and shows an outline of that when it is empty.
     *
     * @param container The bomb the slot belongs to.
     * @param index Which of the three slots this is.
     * @param x The x position of the slot.
     * @param y The y position of the slot.
     */
    inner class AtomicBombSlot(container: Container, index: Int, x: Int, y: Int) : Slot(container, index, x, y) {

        override fun mayPlace(stack: ItemStack) = canPlaceItem(index, stack)

        override fun getMaxStackSize(stack: ItemStack) = Companion.getMaxStackSize(stack)

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

        /**
         * The slot holding the explosive that sets the bomb off.
         */
        val explosiveSlots = intArrayOf(0)

        /**
         * The slot holding the bullet that starts the reaction.
         */
        val bulletSlots = intArrayOf(1)

        /**
         * The slot holding the uranium the bomb is fuelled with.
         */
        val fuelSlots = intArrayOf(2)

        /**
         * Every slot of the bomb, in the order they are drawn.
         */
        val allSlots = intArrayOf(*explosiveSlots, *bulletSlots, *fuelSlots)

        /**
         * How much of an item a bomb slot will hold, which caps the uranium at what the largest bomb needs and everything else at one.
         *
         * @param stack The item being put in.
         * @return How many of it the slot will hold.
         */
        fun getMaxStackSize(stack: ItemStack) = when (stack.item) {
            ToStarsMod.blocks.enriched_uranium_block.asItem() -> min(5, stack.maxStackSize)
            ToStarsMod.items.enriched_uranium.asItem() -> min(45, stack.maxStackSize)
            else -> 1
        }

        /**
         * Whether an item belongs in a particular slot of the bomb.
         *
         * @param slot Which slot it is going into.
         * @param stack The item being put in.
         * @return Returns `true` if it belongs there, otherwise `false`.
         */
        fun canPlaceItem(slot: Int, stack: ItemStack) = when (slot) {
            in explosiveSlots -> stack.`is`(Items.TNT)
            in bulletSlots -> stack.`is`(ToStarsMod.items.enriched_uranium_nugget)
            else -> stack.`is`(ToStarsMod.blocks.enriched_uranium_block.asItem()) || stack.`is`(ToStarsMod.items.enriched_uranium)
        }

    }

}
