package net.jidb.to.base.data.pub.collection.module.recipe

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.RecipeDataCollectionEvent
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleItemRecipe
import net.minecraft.world.level.ItemLike

/**
 * A [GenericRecipeDataCollectionModule] generating a recipe that turns one item into another without a crafting grid, i.e. stonecutting.
 * Cooking is also a single-item recipe as far as vanilla is concerned, but it needs a builder of its own, so it is rejected here and left to [CookingRecipeDataCollectionModule].
 *
 * @param R The type of single item recipe being generated.
 * @property type The recipe type being generated.
 * @property ingredient What the recipe consumes, or `null` for the collection's own item. Defaults to `null`.
 * @param result The item the recipe produces, or `null` for the collection's own item. Defaults to `null`.
 * @property count How many of the result the recipe produces. Defaults to `1`.
 * @param category The recipe book category the recipe appears under. Defaults to miscellaneous.
 * @param id The identifier to save the recipe under, or `null` to let vanilla name it after its result. Defaults to `null`.
 * @param modify A function adjusting the started builder, given the collection and event being generated for.
 * @since 0.3.0
 */
class SingleItemRecipeDataCollectionModule<R : SingleItemRecipe>(val type: RecipeType<R>, protected val ingredient: Ingredient? = null, result: ItemLike? = null, protected val count: Int = 1, category: RecipeCategory = RecipeCategory.MISC, id: String? = null, modify: SingleItemRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> SingleItemRecipeBuilder) : GenericRecipeDataCollectionModule<SingleItemRecipeBuilder>(result, category, id, modify) {

    /**
     * Creates a module generating a recipe that is already built, for the common case where it does not depend on the item it is for.
     *
     * @param type The recipe type being generated.
     * @param builder The recipe to generate.
     * @param ingredient What the recipe consumes, or `null` for the collection's own item. Defaults to `null`.
     * @param result The item the recipe produces, or `null` for the collection's own item. Defaults to `null`.
     * @param count How many of the result the recipe produces. Defaults to `1`.
     * @param id The identifier to save the recipe under, or `null` to let vanilla name it after its result. Defaults to `null`.
     * @param category The recipe book category the recipe appears under. Defaults to miscellaneous.
     * @since 0.3.0
     */
    constructor(type: RecipeType<R>, builder: SingleItemRecipeBuilder, ingredient: Ingredient? = null, result: ItemLike? = null, count: Int = 1, id: String? = null, category: RecipeCategory = RecipeCategory.MISC) : this(type, ingredient, result, count, category, id, { _, _ -> builder })

    override fun createBuilder(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): List<SingleItemRecipeBuilder> {
        val factories: List<(ingredient: Ingredient, category: RecipeCategory, result: ItemLike, count: Int) -> SingleItemRecipeBuilder> = when (type) {
            RecipeType.SMELTING, RecipeType.BLASTING, RecipeType.SMOKING, RecipeType.CAMPFIRE_COOKING -> error("Cooking recipes should use CookingRecipeDataCollectionModule")
            else -> listOf(SingleItemRecipeBuilder::stonecutting)
        }
        return factories.map { it(ingredient ?: Ingredient.of(collection.`object`), category, result ?: collection.`object`, count) }
    }

}
