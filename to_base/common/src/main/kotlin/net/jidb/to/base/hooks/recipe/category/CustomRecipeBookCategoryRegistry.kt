package net.jidb.to.base.hooks.recipe.category

import net.jidb.to.base.api.recipe.CustomRecipeBookCategory

/**
 * Object responsible for storing a centralised list of [CustomRecipeBookCategory].
 * This list is then given to [net.jidb.to.base.mixin.client.ClientRecipeBookMixin] to install the categories into vanilla.
 *
 * @see CustomRecipeBookCategory
 * @see net.jidb.to.base.mixin.client.ClientRecipeBookMixin
 * @since 1.1.0
 */
object CustomRecipeBookCategoryRegistry {

    /**
     * A list of [CustomRecipeBookCategory] objects registered via [addRecipeBookCategory].
     *
     * @since 1.1.0
     */
    val all: List<CustomRecipeBookCategory> field = mutableListOf()

    /**
     * Adds a [CustomRecipeBookCategory] to the internal list of categories, ready to be registered in [net.jidb.to.base.mixin.client.ClientRecipeBookMixin.recipeCategoryHook]
     *
     * @param category The custom recipe book category to be added.
     * @since 1.1.0
     */
    fun addRecipeBookCategory(category: CustomRecipeBookCategory) {
        all.add(category)
    }

}
