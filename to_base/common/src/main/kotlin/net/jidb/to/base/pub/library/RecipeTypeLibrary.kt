package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType

open class RecipeTypeLibrary(modid: String) : SimpleRegistryLibrary<RecipeType<*>>(modid) {

    override val registry = BuiltInRegistries.RECIPE_TYPE

    operator fun <T : Recipe<*>> invoke(id: Identifier? = null): Library<RecipeType<*>, RecipeType<*>>.LibraryEntry<out RecipeType<T>, out RecipeType<T>> = invoke(::i) {
        val string = id?.toString() ?: it.id.toString()
        object : RecipeType<T> {

            override fun toString() = string

        }
    }

}
