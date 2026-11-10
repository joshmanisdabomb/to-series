package net.jidb.to.stars.client.gui.components

import net.jidb.to.base.client.pub.recipe.FakeClientRecipeBook
import net.jidb.to.base.mixin.client.GhostSlotsAccessor
import net.jidb.to.base.mixin.client.RecipeBookComponentAccessor
import net.jidb.to.stars.inventory.menu.EnergyFurnaceMenu
import net.minecraft.client.gui.components.WidgetSprites
import net.minecraft.client.gui.screens.recipebook.GhostSlots
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent
import net.minecraft.client.gui.screens.recipebook.RecipeCollection
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.util.context.ContextMap
import net.minecraft.world.entity.player.StackedItemContents
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay
import net.minecraft.world.item.crafting.display.RecipeDisplay

/**
 * The recipe book shown beside an energy furnace's interface, which lists the smelting recipes.
 *
 * Vanilla keeps whether the book is open and filtered on the player, against its own fixed set of book types; there is no room there for a modded one, so a stand-in book is put in its place and those settings are kept on this component instead.
 *
 * @param menu The interface the book belongs to.
 * @property filterName What the filter button says it filters by.
 * @param tabInfos The tabs the book is divided into.
 */
class EnergyFurnaceRecipeBookComponent(menu: EnergyFurnaceMenu, val filterName: Component, tabInfos: List<TabInfo>) : RecipeBookComponent<EnergyFurnaceMenu>(menu, tabInfos) {

    init {
        (this as RecipeBookComponentAccessor).`to_base$setBook`(FakeClientRecipeBook())
    }

    override fun isCraftingSlot(slot: Slot) = slot.index in menu.inputSlots || slot.index in menu.outputSlots

    override fun selectMatchingRecipes(collection: RecipeCollection, stackedContents: StackedItemContents) {
        collection.selectRecipes(stackedContents) { it is FurnaceRecipeDisplay }
    }

    override fun fillGhostRecipe(ghosts: GhostSlots, recipe: RecipeDisplay, context: ContextMap) {
        if (recipe is FurnaceRecipeDisplay) {
            (ghosts as GhostSlotsAccessor).`to_base$setInput`(menu.getSlot(menu.inputSlots.first()), context, recipe.ingredient)
            (ghosts as GhostSlotsAccessor).`to_base$setInput`(menu.getSlot(menu.outputSlots.first()), context, recipe.result)
        }
    }

    override fun sendUpdateSettings() = Unit

    override fun getFilterButtonTextures() = filterSprites

    override fun getRecipeFilterName() = filterName

    companion object {

        /**
         * The textures of the filter button, in each of its four states.
         */
        private val filterSprites = WidgetSprites(
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled"),
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled"),
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled_highlighted"),
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled_highlighted")
        )

    }

}
