package net.jidb.to.stars.content

import net.jidb.to.base.pub.library.CustomRecipeBookCategoryLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.recipe.category.KilnCustomRecipeBookCategory
import net.minecraft.world.item.crafting.RecipeBookCategories

/**
 * [CustomRecipeBookCategoryLibrary] implementation holding the custom recipe book categories of this mod.
 *
 * @see net.jidb.to.base.api.recipe.CustomRecipeBookCategory
 */
object ToStarsRecipeCustomCategoryLibrary : CustomRecipeBookCategoryLibrary(ToStarsMod.modid) {

    /**
     * All recipes that can be used in a Kiln.
     */
    val kiln_all by this { KilnCustomRecipeBookCategory(listOf(RecipeBookCategories.FURNACE_BLOCKS, RecipeBookCategories.FURNACE_MISC)) }

    /**
     * Block recipes that can be used in a Kiln.
     */
    val kiln_blocks by this { KilnCustomRecipeBookCategory(listOf(RecipeBookCategories.FURNACE_BLOCKS)) }

    /**
     * Miscellaneous recipes that can be used in a Kiln.
     */
    val kiln_misc by this { KilnCustomRecipeBookCategory(listOf(RecipeBookCategories.FURNACE_MISC)) }

}
