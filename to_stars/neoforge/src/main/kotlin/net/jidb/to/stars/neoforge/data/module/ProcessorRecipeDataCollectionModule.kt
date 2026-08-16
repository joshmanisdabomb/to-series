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

        val fake = object : RecipeOutput {

            override fun accept(key: ResourceKey<Recipe<*>>, recipe: Recipe<*>, advancement: AdvancementHolder?, vararg conditions: ICondition) = Unit

            override fun advancement() = Advancement.Builder()

            override fun includeRootAdvancement() = Unit

        }

    }

}
