package net.jidb.to.base.data.pub.collection.module.recipe

import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.RecipeDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

/**
 * A [GenericRecipeDataCollectionModule] that generates and saves a [SmithingTransformRecipeBuilder] for blocks and items in a collection.
 *
 * @property type The recipe type being generated.
 * @property ingredient What the recipe consumes, or `null` for the collection's own item. Defaults to `null`.
 * @param result The item the recipe produces, or `null` for the collection's own item. Defaults to `null`.
 * @property count How many of the result the recipe produces. Defaults to `1`.
 * @param category The recipe book category the recipe appears under. Defaults to miscellaneous.
 * @param id The identifier to save the recipe under, or `null` to let vanilla name it after its result. Defaults to `null`.
 * @param modify A function adjusting the started builder, given the collection and event being generated for.
 * @since 1.1.0
 */
class SmithingRecipeDataCollectionModule(protected val template: Ingredient?, protected val base: Ingredient?, protected val addition: Ingredient?, protected val result: ItemStackTemplate?, protected val category: RecipeCategory = RecipeCategory.MISC, protected val id: String? = null, protected val modify: SmithingTransformRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> SmithingTransformRecipeBuilder) : DataCollectionModule() {

    override fun generateRecipes(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): Boolean {
        val result = result ?: ItemStackTemplate(collection.`object`)

        SmithingTransformRecipeBuilder(
            template ?: Ingredient.of(collection.`object`),
            base ?: Ingredient.of(collection.`object`),
            addition ?: Ingredient.of(collection.`object`),
            category,
            result
        )
            .apply { event.helper.createHas(this, collection.`object`) }
            .let { it.modify(collection, event) }
            .save(event.output, id?.let { ResourceKey.create(Registries.RECIPE, Identifier.parse(it)) } ?: ResourceKey.create(Registries.RECIPE, result.item.value().identifier))

        return true
    }

}
