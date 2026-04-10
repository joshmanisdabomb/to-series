package net.jidb.to.base.data.collection.module.recipe

import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.event.RecipeDataCollectionEvent
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

class ShapedRecipeDataCollectionModule(result: ItemLike? = null, protected val count: Int = 1, category: RecipeCategory = RecipeCategory.MISC, modify: ShapedRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> ShapedRecipeBuilder) : GenericRecipeDataCollectionModule<ShapedRecipeBuilder>(result, category, modify) {

    constructor(builder: ShapedRecipeBuilder, result: ItemLike? = null, count: Int = 1, category: RecipeCategory = RecipeCategory.MISC) : this(result, count, category, { _, _ -> builder })

    override fun createBuilder(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): List<ShapedRecipeBuilder> {
        return listOf(ShapedRecipeBuilder.shaped(event.registry, category, result ?: collection.`object`, count))
    }

}