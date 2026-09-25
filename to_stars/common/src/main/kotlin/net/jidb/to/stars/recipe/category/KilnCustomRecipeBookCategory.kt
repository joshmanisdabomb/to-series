package net.jidb.to.stars.recipe.category

import net.jidb.to.base.api.recipe.CustomRecipeBookCategory
import net.minecraft.client.gui.screens.recipebook.RecipeCollection
import net.minecraft.world.item.crafting.RecipeBookCategories
import net.minecraft.world.item.crafting.RecipeBookCategory
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry
import net.minecraft.world.item.crafting.display.SlotDisplay

/**
 * Represents a custom recipe book category specifically tailored for recipes that can go in a kiln.
 *
 * @param source A list of existing recipe book categories that should be filtered using [unkilnableCategories] as an exclusion source.
 * @since 0.2.0
 * @see net.jidb.to.stars.inventory.menu.KilnMenu
 * @see net.jidb.to.stars.block.entity.KilnBlockEntity
 */
class KilnCustomRecipeBookCategory(private val source: List<RecipeBookCategory>) : CustomRecipeBookCategory {

    override fun getRecipes(recipes: Map<RecipeBookCategory, List<List<RecipeDisplayEntry>>>): List<RecipeCollection> {
        val all = recipes.values
        val unkilnable = all.flatten().flatten().filter { it.display is FurnaceRecipeDisplay && it.category in unkilnableCategories }.map { (it.display as FurnaceRecipeDisplay).ingredient }.toSet()

        val scoped = source.mapNotNull { recipes[it] }.flatten()
        val kilnable = scoped.filter { it.all { it.display is FurnaceRecipeDisplay && isDisplayKilnable((it.display as FurnaceRecipeDisplay).ingredient, unkilnable) } }

        return kilnable.map(::RecipeCollection)
    }

    /**
     * Determines whether a given [SlotDisplay] is suitable for kiln processing based on a set of restricted (`unkilnable`) displays.
     * The function recursively checks composite displays by examining their contents.
     *
     * @param ingredient The [SlotDisplay] being checked for kiln compatibility.
     * @param unkilnable A set of [SlotDisplay] objects that are incompatible with kiln processing.
     * @return `true` if the provided [ingredient] can be processed in the kiln, otherwise `false`.
     * @since 0.2.0
     */
    private fun isDisplayKilnable(ingredient: SlotDisplay, unkilnable: Set<SlotDisplay>): Boolean {
        if (ingredient is SlotDisplay.Composite) {
            val displays = unkilnable.mapNotNull { (it as? SlotDisplay.Composite)?.contents }.flatten().toSet()
            return ingredient.contents.all { isDisplayKilnable(it, displays) }
        }
        return ingredient !in unkilnable
    }

    companion object {

        /**
         * A predefined set of [RecipeBookCategory] values that represent categories of recipes which are not compatible for processing in the kiln.
         * This set is used to filter out recipes that should be excluded from kiln processing.
         *
         * @since 0.2.0
         */
        val unkilnableCategories = setOf(RecipeBookCategories.BLAST_FURNACE_BLOCKS, RecipeBookCategories.BLAST_FURNACE_MISC, RecipeBookCategories.SMOKER_FOOD, RecipeBookCategories.CAMPFIRE)

    }

}
