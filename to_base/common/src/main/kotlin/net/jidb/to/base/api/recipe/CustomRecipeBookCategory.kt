package net.jidb.to.base.api.recipe

import net.minecraft.client.gui.screens.recipebook.RecipeCollection
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory
import net.minecraft.world.item.crafting.RecipeBookCategory
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry

/**
 * Represents a custom category in a recipe book, extending from the standard recipe book categories.
 * This interface provides a method that is called to determine recipes that should show up in the book category page.
 *
 * @since 1.1.0
 */
fun interface CustomRecipeBookCategory : ExtendedRecipeBookCategory {

    /**
     * This function returns a list of recipe collections for use in a custom recipe book category.
     *
     * @param recipes A map of all recipe display entries for the level, grouped by its current recipe book category.
     * @return a list of RecipeCollection objects for this category, the object being mapped from the [recipes] parameter.
     *
     * @since 1.1.0
     */
    fun getRecipes(recipes: Map<RecipeBookCategory, List<List<RecipeDisplayEntry>>>): List<RecipeCollection>

}
