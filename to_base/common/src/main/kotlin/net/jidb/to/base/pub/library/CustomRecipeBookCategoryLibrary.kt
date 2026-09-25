package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.api.recipe.CustomRecipeBookCategory
import net.jidb.to.base.hooks.recipe.category.CustomRecipeBookCategoryRegistry

/**
 * [SimpleLibrary] implementation to manage custom recipe book categories.
 *
 * [CustomRecipeBookCategory] objects initialised here will be automatically added to [CustomRecipeBookCategoryRegistry] and added to the vanilla recipe book category handler.
 *
 * @param modid The mod ID associated with this library.
 * @since 1.1.0
 */
open class CustomRecipeBookCategoryLibrary(modid: String) : SimpleLibrary<CustomRecipeBookCategory>(modid) {

    override fun afterBuild(entry: Library<CustomRecipeBookCategory, CustomRecipeBookCategory>.LibraryEntry<out CustomRecipeBookCategory, out CustomRecipeBookCategory>) {
        CustomRecipeBookCategoryRegistry.addRecipeBookCategory(entry.value)
    }

}
