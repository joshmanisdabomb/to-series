package net.jidb.to.base.data.collection.module.recipe

import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.event.RecipeDataCollectionEvent
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleItemRecipe
import net.minecraft.world.level.ItemLike

class SingleItemRecipeDataCollectionModule<R : SingleItemRecipe>(val type: RecipeType<R>, protected val ingredient: Ingredient? = null, result: ItemLike? = null, protected val count: Int = 1, category: RecipeCategory = RecipeCategory.MISC, modify: SingleItemRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> SingleItemRecipeBuilder) : GenericRecipeDataCollectionModule<SingleItemRecipeBuilder>(result, category, modify) {

    constructor(type: RecipeType<R>, builder: SingleItemRecipeBuilder, ingredient: Ingredient? = null, result: ItemLike? = null, count: Int = 1, category: RecipeCategory = RecipeCategory.MISC) : this(type, ingredient, result, count, category, { _, _ -> builder })

    override fun createBuilder(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): List<SingleItemRecipeBuilder> {
        val factories: List<(ingredient: Ingredient, category: RecipeCategory, result: ItemLike, count: Int) -> SingleItemRecipeBuilder> = when (type) {
            RecipeType.SMELTING, RecipeType.BLASTING, RecipeType.SMOKING, RecipeType.CAMPFIRE_COOKING -> error("Cooking recipes should use CookingRecipeDataCollectionModule")
            else -> listOf(SingleItemRecipeBuilder::stonecutting)
        }
        return factories.map { it(ingredient ?: Ingredient.of(collection.`object`), category, result ?: collection.`object`, count) }
    }

}