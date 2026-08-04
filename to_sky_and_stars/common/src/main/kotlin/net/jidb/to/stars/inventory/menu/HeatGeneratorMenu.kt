package net.jidb.to.stars.inventory.menu

import net.jidb.to.base.api.inventory.data.ContainerDataSchema
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * The interface a machine that makes heat is opened into, which has one slot for the fuel it burns.
 *
 * @param type The menu type being built.
 * @param id The id of the menu.
 * @property playerInventory The inventory of the player who opened it.
 * @property container The generator the menu is opened on.
 * @property data The generator's figures, as the interface reads them.
 */
abstract class HeatGeneratorMenu(type: MenuType<*>, id: Int, protected val playerInventory: Inventory, internal val container: Container, internal val data: ContainerData) : AbstractContainerMenu(type, id) {

    init {
        checkContainerSize(container, allSlots.size)

        container.startOpen(playerInventory.player)

        addSlot(GeneratorFuelSlot(container, 0, 69, 41))

        addStandardInventorySlots(playerInventory, 8, 72)

        checkContainerDataCount(data, dataSchema.getDataSize())

        addDataSlots(data)
    }

    /**
     * Whether an item can go in the fuel slot of this generator.
     *
     * @param stack The item being put in.
     * @param level The level the generator is in.
     * @return Returns `true` if it will burn, otherwise `false`.
     */
    abstract fun canPlaceItem(stack: ItemStack, level: Level): Boolean

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
     * How much heat an item is worth to this generator, against a fuel of ordinary value.
     *
     * @param stack The item being burned.
     * @param level The level the generator is in.
     * @return How much it is worth, or `0` where it will not burn at all.
     */
    abstract fun getFuelValue(stack: ItemStack, level: Level): Float

    /**
     * How long an item burns for in this generator, before its own speed applies.
     *
     * @param stack The item being burned.
     * @param level The level the generator is in.
     * @return How long it burns for, in ticks, or `0` where it will not burn at all.
     */
    abstract fun getFuelDuration(stack: ItemStack, level: Level): Short

    /**
     * The generator's fuel slot, which only accepts what it will actually burn.
     *
     * @param container The generator the slot belongs to.
     * @param index Which slot this is.
     * @param x The x position of the slot.
     * @param y The y position of the slot.
     */
    inner class GeneratorFuelSlot(container: Container, index: Int, x: Int, y: Int) : Slot(container, index, x, y) {

        override fun mayPlace(stack: ItemStack) = canPlaceItem(stack, playerInventory.player.level())

        override fun setChanged() {
            super.setChanged()
            this@HeatGeneratorMenu.slotsChanged(container)
        }

    }

    companion object {

        /**
         * The slot holding the fuel the generator burns.
         */
        val fuelSlots = intArrayOf(0)

        /**
         * Every slot of the generator, in the order they are drawn.
         */
        val allSlots = intArrayOf(*fuelSlots)

        /**
         * How the generator's figures are laid out across the shorts a menu can send, since each one is too large or too precise to fit in a single short by itself.
         */
        val dataSchema = ContainerDataSchema<HeatGeneratorDataKey>()
            .defineDecimal(HeatGeneratorDataKey.HEAT)
            .defineDecimal(HeatGeneratorDataKey.CHANGE, 1, 3)
            .defineDecimal(HeatGeneratorDataKey.ADD, 1, 4)
            .defineShort(HeatGeneratorDataKey.DURATION)
            .defineShort(HeatGeneratorDataKey.MAX_DURATION)
            .defineDecimal(HeatGeneratorDataKey.SPEED, 1, 3)
            .defineDecimal(HeatGeneratorDataKey.VALUE, 1, 3)
            .defineDecimal(HeatGeneratorDataKey.INITIAL, 1, 1)
            .defineDecimal(HeatGeneratorDataKey.RANGE, 1, 1)
            .defineDecimal(HeatGeneratorDataKey.BONUS, 1, 1)
            .defineDecimal(HeatGeneratorDataKey.COOLING, 1, 4)

    }

    /**
     * Enum that defines each of the figures a generator's interface reads from it.
     */
    enum class HeatGeneratorDataKey {

        /**
         * How hot the generator currently is.
         */
        HEAT,

        /**
         * How much its heat changed on the last tick.
         */
        CHANGE,

        /**
         * How much heat the fuel currently burning adds per tick.
         */
        ADD,

        /**
         * How long the fuel currently burning has left, in ticks.
         */
        DURATION,

        /**
         * How long that fuel lasted to begin with.
         */
        MAX_DURATION,

        /**
         * How long the generator takes over its fuel, as a multiple.
         */
        SPEED,

        /**
         * How much heat it makes per tick from a fuel of ordinary value.
         */
        VALUE,

        /**
         * The heat it settles at while doing nothing.
         */
        INITIAL,

        /**
         * How far above that it can be driven.
         */
        RANGE,

        /**
         * The extra heat it is given for running at its limit.
         */
        BONUS,

        /**
         * What is left of its heat each tick once it stops being fed.
         */
        COOLING

    }

}
