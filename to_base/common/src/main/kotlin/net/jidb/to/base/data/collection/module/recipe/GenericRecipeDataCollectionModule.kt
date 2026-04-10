package net.jidb.to.base.data.collection.module.recipe

import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.event.RecipeDataCollectionEvent
import net.jidb.to.base.data.collection.module.DataCollectionModule
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

abstract class GenericRecipeDataCollectionModule<B : RecipeBuilder>(protected val result: ItemLike?, protected val category: RecipeCategory = RecipeCategory.MISC, protected val modify: B.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> B) : DataCollectionModule() {

    abstract fun createBuilder(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): List<B>

    override fun generateRecipes(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): Boolean {
        createBuilder(collection, event).map { modify(it, collection, event) }.forEach {
            it.save(event.output)
        }
        return true
    }

}