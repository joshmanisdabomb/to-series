package net.jidb.to.base.data.pub.collection.module.recipe

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.RecipeDataCollectionEvent
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.ItemLike

/**
 * A [GenericRecipeDataCollectionModule] generating a cooking recipe, and by default the related ones alongside it.
 *
 * Vanilla's own food is cookable several ways, so naming one type here generates the rest: a campfire or smoker recipe also generates the other two, and a blasting recipe also generates smelting.
 * Each of them keeps its own cooking time, so a smoker recipe is still faster than the furnace one it was generated with. Pass `multiple` as `false` for a recipe that should only work the one way.
 *
 * @param R The type of cooking recipe being generated.
 * @property type The recipe type being generated, which decides which related recipes come with it.
 * @property ingredient What the recipe consumes, or `null` for the collection's own item. Defaults to `null`.
 * @param result The item the recipe produces, or `null` for the collection's own item. Defaults to `null`.
 * @property experience How much experience cooking the recipe awards. Defaults to none.
 * @property time How long each recipe type takes, in ticks. Defaults to the times vanilla uses.
 * @param category The recipe book category the recipe appears under. Defaults to miscellaneous.
 * @property cookingCategory The cooking book category the recipe appears under. Defaults to miscellaneous.
 * @property multiple Whether the related recipe types are generated alongside the named one. Defaults to `true`.
 * @param id The identifier to save the recipe under, or `null` to let vanilla name it after its result. Defaults to `null`.
 * @param modify A function adjusting each started builder, given the collection and event being generated for.
 * @since 0.3.0
 */
class CookingRecipeDataCollectionModule<R : AbstractCookingRecipe>(val type: RecipeType<R>, protected val ingredient: Ingredient? = null, result: ItemLike? = null, protected val experience: Float = 0f, protected val time: (type: RecipeType<out AbstractCookingRecipe>) -> Int = ::getDefaultTime, category: RecipeCategory = RecipeCategory.MISC, protected val cookingCategory: CookingBookCategory = CookingBookCategory.MISC, protected val multiple: Boolean = true, id: String? = null, modify: SimpleCookingRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> SimpleCookingRecipeBuilder) : GenericRecipeDataCollectionModule<SimpleCookingRecipeBuilder>(result, category, id, modify) {

    /**
     * Creates a module generating a recipe that is already built, for the common case where it does not depend on the item it is for.
     *
     * @param type The recipe type being generated, which decides which related recipes come with it.
     * @param builder The recipe to generate.
     * @param ingredient What the recipe consumes, or `null` for the collection's own item. Defaults to `null`.
     * @param result The item the recipe produces, or `null` for the collection's own item. Defaults to `null`.
     * @param experience How much experience cooking the recipe awards. Defaults to none.
     * @param time How long each recipe type takes, in ticks. Defaults to the times vanilla uses.
     * @param category The recipe book category the recipe appears under. Defaults to miscellaneous.
     * @param cookingCategory The cooking book category the recipe appears under.
     * @param id The identifier to save the recipe under, or `null` to let vanilla name it after its result. Defaults to `null`.
     * @param multiple Whether the related recipe types are generated alongside the named one. Defaults to `true`.
     * @since 0.3.0
     */
    constructor(type: RecipeType<R>, builder: SimpleCookingRecipeBuilder, ingredient: Ingredient? = null, result: ItemLike? = null, experience: Float = 0f, time: (type: RecipeType<out AbstractCookingRecipe>) -> Int = ::getDefaultTime, category: RecipeCategory = RecipeCategory.MISC, cookingCategory: CookingBookCategory, id: String? = null, multiple: Boolean = true) : this(type, ingredient, result, experience, time, category, cookingCategory, multiple, id, { _, _ -> builder })

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

        /**
         * How long a recipe of the given type takes by default, matching the times vanilla's own recipes use.
         *
         * @param type The recipe type to get a time for.
         * @return The cooking time, in ticks.
         * @since 0.3.0
         */
        fun getDefaultTime(type: RecipeType<out AbstractCookingRecipe>) = when (type) {
            RecipeType.SMOKING, RecipeType.BLASTING -> 100
            RecipeType.CAMPFIRE_COOKING -> 600
            else -> 200
        }

    }

}
