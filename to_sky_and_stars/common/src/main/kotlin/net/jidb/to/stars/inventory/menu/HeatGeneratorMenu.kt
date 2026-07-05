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

abstract class HeatGeneratorMenu(type: MenuType<*>, id: Int, protected val playerInventory: Inventory, internal val container: Container, internal val data: ContainerData) : AbstractContainerMenu(type, id) {

    init {
        checkContainerSize(container, allSlots.size)

        container.startOpen(playerInventory.player)

        addSlot(GeneratorFuelSlot(container, 0, 69, 41))

        addStandardInventorySlots(playerInventory, 8, 72)

        checkContainerDataCount(data, dataSchema.getDataSize())

        addDataSlots(data)
    }

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

    abstract fun getFuelValue(stack: ItemStack, level: Level): Float
    abstract fun getFuelDuration(stack: ItemStack, level: Level): Short

    inner class GeneratorFuelSlot(container: Container, index: Int, x: Int, y: Int) : Slot(container, index, x, y) {

        override fun mayPlace(stack: ItemStack) = canPlaceItem(stack, playerInventory.player.level())

        override fun setChanged() {
            super.setChanged()
            this@HeatGeneratorMenu.slotsChanged(container)
        }

    }

    companion object {
        val fuelSlots = intArrayOf(0)
        val allSlots = intArrayOf(*fuelSlots)

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

    enum class HeatGeneratorDataKey {
        HEAT,
        CHANGE,
        ADD,
        DURATION,
        MAX_DURATION,
        SPEED,
        VALUE,
        INITIAL,
        RANGE,
        BONUS,
        COOLING;
    }

}
