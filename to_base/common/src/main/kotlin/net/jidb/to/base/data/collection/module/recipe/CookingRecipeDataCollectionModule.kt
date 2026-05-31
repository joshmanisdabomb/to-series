package net.jidb.to.base.data.collection.module.recipe

import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.event.RecipeDataCollectionEvent
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.ItemLike

class CookingRecipeDataCollectionModule<R : AbstractCookingRecipe>(val type: RecipeType<R>, protected val ingredient: Ingredient? = null, result: ItemLike? = null, protected val experience: Float = 0f, protected val time: (type: RecipeType<out AbstractCookingRecipe>) -> Int = ::getDefaultTime, category: RecipeCategory = RecipeCategory.MISC, protected val cookingCategory: CookingBookCategory = CookingBookCategory.MISC, protected val multiple: Boolean = true, modify: SimpleCookingRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> SimpleCookingRecipeBuilder) : GenericRecipeDataCollectionModule<SimpleCookingRecipeBuilder>(result, category, modify) {

    constructor(type: RecipeType<R>, builder: SimpleCookingRecipeBuilder, ingredient: Ingredient? = null, result: ItemLike? = null, experience: Float = 0f, time: (type: RecipeType<out AbstractCookingRecipe>) -> Int = ::getDefaultTime, category: RecipeCategory = RecipeCategory.MISC, cookingCategory: CookingBookCategory, multiple: Boolean = true) : this(type, ingredient, result, experience, time, category, cookingCategory, multiple, { _, _ -> builder })

    override fun createBuilder(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): List<SimpleCookingRecipeBuilder> {
        val factories: Map<RecipeType<out AbstractCookingRecipe>, (ingredient: Ingredient, category: RecipeCategory, cookingCategory: CookingBookCategory, result: ItemLike, experience: Float, time: Int) -> SimpleCookingRecipeBuilder> = when (type) {
            RecipeType.CAMPFIRE_COOKING -> mapOf(
                RecipeType.CAMPFIRE_COOKING to { ingredient: Ingredient, category: RecipeCategory, cookingCategory: CookingBookCategory, result: ItemLike, experience: Float, time: Int -> SimpleCookingRecipeBuilder.campfireCooking(ingredient, category, result, experience, time) },
                RecipeType.SMOKING to { ingredient: Ingredient, category: RecipeCategory, cookingCategory: CookingBookCategory, result: ItemLike, experience: Float, time: Int -> SimpleCookingRecipeBuilder.smoking(ingredient, category, result, experience, time) },
                RecipeType.SMELTING to SimpleCookingRecipeBuilder::smelting
            )
            RecipeType.SMOKING -> mapOf(
                RecipeType.SMOKING to { ingredient: Ingredient, category: RecipeCategory, cookingCategory: CookingBookCategory, result: ItemLike, experience: Float, time: Int -> SimpleCookingRecipeBuilder.smoking(ingredient, category, result, experience, time) },
                RecipeType.CAMPFIRE_COOKING to { ingredient: Ingredient, category: RecipeCategory, cookingCategory: CookingBookCategory, result: ItemLike, experience: Float, time: Int -> SimpleCookingRecipeBuilder.campfireCooking(ingredient, category, result, experience, time) },
                RecipeType.SMELTING to SimpleCookingRecipeBuilder::smelting
            )
            RecipeType.BLASTING -> mapOf(
                RecipeType.BLASTING to SimpleCookingRecipeBuilder::blasting,
                RecipeType.SMELTING to SimpleCookingRecipeBuilder::smelting
            )
            else -> mapOf(
                RecipeType.SMELTING to SimpleCookingRecipeBuilder::smelting
            )
        }
        return factories
            .let { if (!multiple) it.toList().take(1).toMap() else it }
            .map { (recipe, factory) -> factory(
                ingredient ?: Ingredient.of(collection.`object`),
                category,
                cookingCategory,
                result ?: collection.`object`,
                experience,
                time(recipe)
            ) }
    }

    companion object {
        fun getDefaultTime(type: RecipeType<out AbstractCookingRecipe>) = when (type) {
            RecipeType.SMOKING, RecipeType.BLASTING -> 100
            RecipeType.CAMPFIRE_COOKING -> 600
            else -> 200
        }
    }

}