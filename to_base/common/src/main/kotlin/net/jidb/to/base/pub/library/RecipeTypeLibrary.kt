package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType

/**
 * [SimpleRegistryLibrary] implementation that registers [RecipeType] content to [BuiltInRegistries.RECIPE_TYPE], and provides access to that content in one place.
 * A recipe type carries no behaviour of its own, so each entry is an anonymous instance that exists only to be compared by identity and to name itself when printed.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.8.0
 */
open class RecipeTypeLibrary(modid: String) : SimpleRegistryLibrary<RecipeType<*>>(modid) {

    override val registry = BuiltInRegistries.RECIPE_TYPE

    /**
     * Declares a recipe type named after the property it is assigned to, in the namespace of this library.
     *
     * @param T The type of recipe this recipe type covers.
     * @param id The identifier the recipe type names itself with, or `null` to use the identifier of the entry. Defaults to `null`.
     * @return The [Library.LibraryEntry] for the declared property.
     * @since 0.8.0
     */
    operator fun <T : Recipe<*>> invoke(id: Identifier? = null): Library<RecipeType<*>, RecipeType<*>>.LibraryEntry<out RecipeType<T>, out RecipeType<T>> = invoke(::i) {
        val string = id?.toString() ?: it.id.toString()
        object : RecipeType<T> {

            override fun toString() = string

        }
    }

}
