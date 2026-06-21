package net.jidb.to.base.data.pub.collection.module.recipe

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.RecipeDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

abstract class GenericRecipeDataCollectionModule<B : RecipeBuilder>(protected val result: ItemLike?, protected val category: RecipeCategory = RecipeCategory.MISC, protected val id: String? = null, protected val modify: B.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> B) : DataCollectionModule() {

    abstract fun createBuilder(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): List<B>

    override fun generateRecipes(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): Boolean {
        createBuilder(collection, event).map { modify(it, collection, event) }.forEach {
            if (id != null) it.save(event.output, id)
            else it.save(event.output)
        }
        return true
    }

}