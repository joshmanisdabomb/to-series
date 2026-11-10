package net.jidb.to.stars.inventory.menu

import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.KilnBlockEntity
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractFurnaceMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.RecipeBookType
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipePropertySet
import net.minecraft.world.level.Level

/**
 * Represents the inventory menu system for the Kiln block and block entity.
 *
 * This class extends [AbstractFurnaceMenu] to provide custom interactions tailored to the kiln's behavior.
 * The kiln restricts allowed recipes to those exclusively valid for standard furnaces while rejecting recipes associated with other cooking equipment, such as smokers and blast furnaces.
 *
 * @constructor Initializes the [KilnMenu] with the specified parameters, defining its inventory, container, and data tracking.
 *
 * @param id The unique container ID used to identify this menu across client and server.
 * @param playerInventory The inventory associated with the player interacting with the kiln.
 * @param container The container backing the inventory slots of the kiln.
 * @param data The container data used to sync data values (e.g. cooking time) from the server to the client.
 * @see net.jidb.to.stars.block.KilnBlock
 * @see KilnBlockEntity
 * @see net.jidb.to.stars.client.gui.screens.KilnScreen
 * @since 0.2.0
 */
class KilnMenu(id: Int, playerInventory: Inventory, container: Container, data: ContainerData) : AbstractFurnaceMenu(ToStarsMod.menus.kiln, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, id, playerInventory, container, data) {

    /**
     * Represents the test of valid kiln items based on the level's recipeAccess.
     * This set is used in [canSmelt] to test that an item has a kiln recipe.
     *
     * @since 0.2.0
     */
    private val tester by lazy { createPropertySetTester(level) }

    /**
     * Client-side constructor for a [KilnMenu].
     * Initializes the [KilnMenu] by creating default placeholder instances of [SimpleContainer] and [SimpleContainerData] to manage item slots and container data, respectively.
     *
     * @param id A unique identifier for the menu instance.
     * @param playerInventory The player's inventory interacting with the menu.
     */
    constructor(id: Int, playerInventory: Inventory) : this(id, playerInventory, SimpleContainer(SLOT_COUNT), SimpleContainerData(DATA_COUNT))

    override fun canSmelt(stack: ItemStack) = tester(stack)

    companion object {

        fun createPropertySetTester(level: Level): (stack: ItemStack) -> Boolean {
            val furnaceSet = level.recipeAccess().propertySet(RecipePropertySet.FURNACE_INPUT)
            val otherSets = listOf(RecipePropertySet.SMOKER_INPUT, RecipePropertySet.BLAST_FURNACE_INPUT, RecipePropertySet.CAMPFIRE_INPUT).map(level.recipeAccess()::propertySet)
            return { stack -> furnaceSet.test(stack) && otherSets.none { it.test(stack) } }
        }

    }

}
