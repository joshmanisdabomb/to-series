package net.jidb.to.base.data.pub.collection.module.recipe

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.RecipeDataCollectionEvent
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

/**
 * A [GenericRecipeDataCollectionModule] generating a shapeless crafting recipe, whose ingredients are filled in by the `modify` function.
 *
 * @param result The item the recipe produces, or `null` for the collection's own item. Defaults to `null`.
 * @property count How many of the result the recipe produces. Defaults to `1`.
 * @param category The recipe book category the recipe appears under. Defaults to miscellaneous.
 * @param id The identifier to save the recipe under, or `null` to let vanilla name it after its result. Defaults to `null`.
 * @param modify A function filling in the recipe's ingredients, given the collection and event being generated for.
 * @since 0.3.0
 */
class ShapelessRecipeDataCollectionModule(result: ItemLike? = null, protected val count: Int = 1, category: RecipeCategory = RecipeCategory.MISC, id: String? = null, modify: ShapelessRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> ShapelessRecipeBuilder) : GenericRecipeDataCollectionModule<ShapelessRecipeBuilder>(result, category, id, modify) {

    /**
     * Creates a module generating a recipe that is already built, for the common case where it does not depend on the item it is for.
     *
     * @param builder The recipe to generate.
     * @param result The item the recipe produces, or `null` for the collection's own item. Defaults to `null`.
     * @param count How many of the result the recipe produces. Defaults to `1`.
     * @param id The identifier to save the recipe under, or `null` to let vanilla name it after its result. Defaults to `null`.
     * @param category The recipe book category the recipe appears under. Defaults to miscellaneous.
     * @since 0.3.0
     */
    constructor(builder: ShapelessRecipeBuilder, result: ItemLike? = null, count: Int = 1, id: String? = null, category: RecipeCategory = RecipeCategory.MISC) : this(result, count, category, id, { _, _ -> builder })

    override fun createBuilder(collection: DataCollection<Item>, event: RecipeDataCollectionEvent) = listOf(ShapelessRecipeBuilder.shapeless(event.registry, category, collection.`object`, count))

}
