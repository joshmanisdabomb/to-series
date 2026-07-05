package net.jidb.to.stars.inventory.menu

import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class SolidGeneratorMenu(id: Int, playerInventory: Inventory, container: Container, data: ContainerData) : HeatGeneratorMenu(ToStarsMod.menus.solid_generator, id, playerInventory, container, data) {

    constructor(id: Int, playerInventory: Inventory) : this(id, playerInventory, SimpleContainer(allSlots.size), SimpleContainerData(dataSchema.getDataSize()))

    override fun canPlaceItem(stack: ItemStack, level: Level) = getFuelDuration(stack, level) > 0

    override fun getFuelValue(stack: ItemStack, level: Level) = Companion.getFuelValue(stack, level)
    override fun getFuelDuration(stack: ItemStack, level: Level) = Companion.getFuelDuration(stack, level)

    companion object {
        fun getFuelValue(stack: ItemStack, level: Level): Float {
            val fuels = level.fuelValues()
            if (!fuels.isFuel(stack)) return 0f
            if (stack.`is`(ToStarsMod.itemTags.heat_generator_2x)) return 2f
            if (stack.`is`(Services.platform.tags.getCommonItem("buckets")!!)) return 0f
            return 1f
        }

        fun getFuelDuration(stack: ItemStack, level: Level): Short {
            val fuels = level.fuelValues()
            if (!fuels.isFuel(stack)) return 0
            if (stack.`is`(Services.platform.tags.getCommonItem("buckets")!!)) return 0
            return fuels.burnDuration(stack).toShort()
        }
    }

}
