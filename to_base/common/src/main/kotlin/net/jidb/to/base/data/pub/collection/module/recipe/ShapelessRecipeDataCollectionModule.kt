package net.jidb.to.base.data.pub.collection.module.recipe

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.RecipeDataCollectionEvent
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

class ShapelessRecipeDataCollectionModule(result: ItemLike? = null, protected val count: Int = 1, category: RecipeCategory = RecipeCategory.MISC, id: String? = null, modify: ShapelessRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> ShapelessRecipeBuilder) : GenericRecipeDataCollectionModule<ShapelessRecipeBuilder>(result, category, id, modify) {

    constructor(builder: ShapelessRecipeBuilder, result: ItemLike? = null, count: Int = 1, id: String? = null, category: RecipeCategory = RecipeCategory.MISC) : this(result, count, category, id, { _, _ -> builder })

    override fun createBuilder(collection: DataCollection<Item>, event: RecipeDataCollectionEvent) = listOf(ShapelessRecipeBuilder.shapeless(event.registry, category, collection.`object`, count))

}
