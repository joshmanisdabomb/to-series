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

/**
 * The interface a solid generator is opened into, which burns the same items a furnace does.
 *
 * @param id The id of the menu.
 * @param playerInventory The inventory of the player who opened it.
 * @param container The generator the menu is opened on.
 * @param data The generator's figures, as the interface reads them.
 */
class SolidGeneratorMenu(id: Int, playerInventory: Inventory, container: Container, data: ContainerData) : HeatGeneratorMenu(ToStarsMod.menus.solid_generator, id, playerInventory, container, data) {

    /**
     * Creates the menu on the client, where the generator itself is not available and an empty container stands in for it.
     *
     * @param id The id of the menu.
     * @param playerInventory The inventory of the player who opened it.
     */
    constructor(id: Int, playerInventory: Inventory) : this(id, playerInventory, SimpleContainer(allSlots.size), SimpleContainerData(dataSchema.getDataSize()))

    override fun canPlaceItem(stack: ItemStack, level: Level) = getFuelDuration(stack, level) > 0

    override fun getFuelValue(stack: ItemStack, level: Level) = Companion.getFuelValue(stack, level)
    override fun getFuelDuration(stack: ItemStack, level: Level) = Companion.getFuelDuration(stack, level)

    companion object {

        /**
         * How much heat a furnace fuel is worth to a solid generator, which is double for anything tagged as burning hot and nothing at all for a bucket, since the bucket itself would be left behind.
         *
         * @param stack The item being burned.
         * @param level The level the generator is in.
         * @return How much it is worth, or `0` where it will not burn at all.
         */
        fun getFuelValue(stack: ItemStack, level: Level): Float {
            val fuels = level.fuelValues()
            if (!fuels.isFuel(stack)) return 0f
            if (stack.`is`(ToStarsMod.itemTags.heat_generator_2x)) return 2f
            if (stack.`is`(Services.platform.tags.getCommonItem("buckets")!!)) return 0f
            return 1f
        }

        /**
         * How long a furnace fuel burns for in a solid generator, which is however long a furnace would burn it, save for a bucket, which will not burn at all.
         *
         * @param stack The item being burned.
         * @param level The level the generator is in.
         * @return How long it burns for, in ticks, or `0` where it will not burn at all.
         */
        fun getFuelDuration(stack: ItemStack, level: Level): Short {
            val fuels = level.fuelValues()
            if (!fuels.isFuel(stack)) return 0
            if (stack.`is`(Services.platform.tags.getCommonItem("buckets")!!)) return 0
            return fuels.burnDuration(stack).toShort()
        }

    }

}
