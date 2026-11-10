package net.jidb.to.base.hooks.recipe

import net.minecraft.resources.ResourceKey
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeMap
import net.minecraft.world.item.crafting.RecipePropertySet

/**
 * Object responsible for storing a centralised list of ingredient extractors from a list of recipes.
 * This list is then given to [net.jidb.to.base.mixin.RecipeManagerMixin] to set the properties on the server and then synchronise them to the client.
 *
 * @see net.jidb.to.base.mixin.RecipeManagerMixin
 * @since 1.1.0
 */
object RecipePropertySetRegistry {

    /**
     * A map of ingredient extractor objects registered via [add].
     *
     * @since 1.1.0
     */
    val all: Map<ResourceKey<RecipePropertySet>, (recipe: Recipe<*>) -> List<Ingredient>> field = mutableMapOf()

    fun add(key: ResourceKey<RecipePropertySet>, extractor: (recipe: Recipe<*>) -> List<Ingredient>) {
        all[key] = extractor
    }

    fun buildPropertySets(recipes: RecipeMap, flags: FeatureFlagSet) = all.mapValues { (key, value) ->
        val ingredients = recipes.values().flatMap { value(it.value) }.filter { it.items().allMatch { it.value().isEnabled(flags) } }
        RecipePropertySet.create(ingredients)
    }

}
