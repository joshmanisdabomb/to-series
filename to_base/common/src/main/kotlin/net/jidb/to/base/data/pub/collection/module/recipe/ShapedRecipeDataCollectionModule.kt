package net.jidb.to.base.data.pub.collection.module.recipe

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.BlockLootDataCollectionEvent
import net.jidb.to.base.data.api.collection.event.RecipeDataCollectionEvent
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable

class ShapedRecipeDataCollectionModule(result: ItemLike? = null, protected val count: Int = 1, category: RecipeCategory = RecipeCategory.MISC, id: String? = null, modify: ShapedRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> ShapedRecipeBuilder) : GenericRecipeDataCollectionModule<ShapedRecipeBuilder>(result, category, id, modify) {

    constructor(builder: ShapedRecipeBuilder, result: ItemLike? = null, count: Int = 1, id: String? = null, category: RecipeCategory = RecipeCategory.MISC) : this(result, count, category, id, { _, _ -> builder })

    override fun createBuilder(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): List<ShapedRecipeBuilder> {
        return listOf(ShapedRecipeBuilder.shaped(event.registry, category, result ?: collection.`object`, count))
    }

    override fun generateBlockLoot(collection: DataCollection<Block>, event: BlockLootDataCollectionEvent): Map<Block, LootTable.Builder>? {
        return super.generateBlockLoot(collection, event)
    }

}