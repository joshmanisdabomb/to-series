package net.jidb.to.base.data.pub.collection.module.recipe

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.RecipeDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

/**
 * The base class for a [DataCollectionModule] generating recipes, which builds one or more recipes and saves each of them.
 * A subclass says only how to start the builder; adjusting it into the actual recipe is left to the caller through [modify], so that the shape of a recipe stays where the content is declared rather than in a module per recipe.
 *
 * @param B The type of recipe builder this module generates through.
 * @property result The item the recipe produces, or `null` for the collection's own item.
 * @property category The recipe book category the recipe appears under. Defaults to miscellaneous.
 * @property id The identifier to save the recipe under, or `null` to let vanilla name it after its result. Defaults to `null`.
 * @property modify A function turning the started builder into the finished recipe, given the collection and event being generated for.
 * @since 0.3.0
 */
abstract class GenericRecipeDataCollectionModule<B : RecipeBuilder>(protected val result: ItemLike?, protected val category: RecipeCategory = RecipeCategory.MISC, protected val id: String? = null, protected val modify: B.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> B) : DataCollectionModule() {

    /**
     * Starts the builders this module generates from, one per recipe it produces.
     *
     * @param collection The collection being generated for.
     * @param event The event being generated for.
     * @return The recipe builders, before [modify] has been applied to each of them.
     * @since 0.3.0
     */
    abstract fun createBuilder(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): List<B>

    override fun generateRecipes(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): Boolean {
        createBuilder(collection, event).map { modify(it, collection, event) }.forEach {
            if (id != null) it.save(event.output, id)
            else it.save(event.output)
        }
        return true
    }

}
