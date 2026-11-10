package net.jidb.to.stars.inventory.menu

import net.jidb.to.stars.block.entity.ProcessorBlockEntity
import net.jidb.to.stars.recipe.processor.ProcessorRecipe
import net.jidb.to.stars.recipe.processor.ProcessorRecipeInput
import net.minecraft.recipebook.ServerPlaceRecipe
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.StackedItemContents
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level

/**
 * The interface a machine that runs processor recipes is opened into, which is backed by the recipe book so that a recipe can be filled in from it.
 *
 * @param type The menu type being built.
 * @param id The id of the menu.
 * @property playerInventory The inventory of the player who opened it.
 * @property container The machine the menu is opened on.
 * @property data The machine's figures, as the interface reads them.
 */
abstract class ProcessorMenu(type: MenuType<*>, id: Int, playerInventory: Inventory, container: Container, data: ContainerData) : AbstractProcessorMenu(type, id, playerInventory, container, data) {

    protected var _clientData: AbstractProcessorMenuData? = null

    override fun handlePlacement(useMaxItems: Boolean, allowDroppingItemsToClear: Boolean, recipe: RecipeHolder<*>, level: ServerLevel, inventory: Inventory): PostPlaceAction = ServerPlaceRecipe.placeRecipe(object : ServerPlaceRecipe.CraftingMenuAccess<ProcessorRecipe> {

        override fun fillCraftSlotsStackedContents(stackedContents: StackedItemContents) = this@ProcessorMenu.fillCraftSlotsStackedContents(stackedContents)

        override fun clearCraftingContent() {
            inputSlots.forEach { this@ProcessorMenu.getSlot(it).set(ItemStack.EMPTY) }
        }

        override fun recipeMatches(recipe: RecipeHolder<ProcessorRecipe>) = recipe.value().matches(ProcessorRecipeInput((container as ProcessorBlockEntity).blockState, inputSlots.map(container::getItem)), level)

    }, inputSlots.size, 1, inputSlots.map(::getSlot), inputSlots.map(::getSlot), inventory, recipe as RecipeHolder<ProcessorRecipe>, useMaxItems, allowDroppingItemsToClear)

    abstract override fun canPlaceInput(stack: ItemStack, index: Int, level: Level): Boolean

    override fun getClientData() = _clientData

}
