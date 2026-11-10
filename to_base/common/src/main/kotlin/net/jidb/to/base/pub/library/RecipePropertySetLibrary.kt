package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.AdvancedLibrary
import net.jidb.to.base.api.library.IResourceKeyLibrary
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.hooks.recipe.RecipePropertySetRegistry
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipePropertySet

open class RecipePropertySetLibrary(modid: String) : AdvancedLibrary<(recipe: Recipe<*>) -> List<Ingredient>, ResourceKey<RecipePropertySet>>(modid), IResourceKeyLibrary<(recipe: Recipe<*>) -> List<Ingredient>, ResourceKey<RecipePropertySet>, RecipePropertySet> {

    override val registryKey get() = RecipePropertySet.TYPE_KEY

    operator fun invoke(extractor: (recipe: Recipe<*>) -> List<Ingredient>): Library<(recipe: Recipe<*>) -> List<Ingredient>, ResourceKey<RecipePropertySet>>.LibraryEntry<(recipe: Recipe<*>) -> List<Ingredient>, ResourceKey<RecipePropertySet>> {
        return this.LibraryEntry({
            val location = ResourceKey.create(registryKey, id)
            RecipePropertySetRegistry.add(location, it());
            { location }
        }, { extractor })
    }

}
