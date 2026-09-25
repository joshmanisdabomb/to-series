package net.jidb.to.stars.neoforge.data.module

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.GeneralLootDataCollectionEvent
import net.jidb.to.base.data.api.collection.event.RecipeDataCollectionEvent
import net.jidb.to.base.data.pub.collection.module.recipe.GenericRecipeDataCollectionModule
import net.jidb.to.stars.neoforge.data.recipe.ProcessorRecipeBuilder
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.storage.loot.LootTable
import net.neoforged.neoforge.common.conditions.ICondition

/**
 * A [net.jidb.to.base.data.pub.collection.module.recipe.GenericRecipeDataCollectionModule] generating a processor recipe.
 *
 * A processor recipe draws its results from a loot table rather than writing them into the recipe file, so the same builder is run twice: once to write the recipe and once to write the table alongside it, under a name matching the recipe's own.
 *
 * @param category The section of the recipe book the recipe is filed under. Defaults to miscellaneous.
 * @param id The name to write the recipe under, or `null` to name it after its first result. Defaults to `null`.
 * @param modify Builds the recipe, given the collection and event being generated for.
 */
class ProcessorRecipeDataCollectionModule(category: RecipeCategory = RecipeCategory.MISC, id: String? = null, modify: ProcessorRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> ProcessorRecipeBuilder) : GenericRecipeDataCollectionModule<ProcessorRecipeBuilder>(null, category, id, modify) {

    override fun createBuilder(collection: DataCollection<Item>, event: RecipeDataCollectionEvent) = listOf(ProcessorRecipeBuilder(event.registry))

    override fun generateGeneralLoot(collection: DataCollection<*>, event: GeneralLootDataCollectionEvent): Map<Identifier, LootTable.Builder>? {
        if (collection.`object` !is Item) return null
        val rev = RecipeDataCollectionEvent(event.helper.provider, fake)
        return createBuilder(collection as DataCollection<Item>, rev).associate {
            val recipe = modify(it, collection, rev)
            (id?.let(Identifier::parse) ?: recipe.defaultId().identifier()).withPrefix("processor/") to recipe.getLootTable()
        }
    }

    companion object {

        /**
         * A recipe output that discards everything, used while the builder is run a second time to get at its loot table, since the recipe itself has already been written.
         */
        val fake = object : RecipeOutput {

            override fun accept(key: ResourceKey<Recipe<*>>, recipe: Recipe<*>, advancement: AdvancementHolder?, vararg conditions: ICondition) = Unit

            override fun advancement() = Advancement.Builder()

            override fun includeRootAdvancement() = Unit

        }

    }

}
