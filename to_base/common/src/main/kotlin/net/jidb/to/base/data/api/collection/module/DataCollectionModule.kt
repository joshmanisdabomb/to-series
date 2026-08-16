package net.jidb.to.base.data.api.collection.module

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.BlockLootDataCollectionEvent
import net.jidb.to.base.data.api.collection.event.BlockTagDataCollectionEvent
import net.jidb.to.base.data.api.collection.event.ConfiguredFeatureDataCollectionEvent
import net.jidb.to.base.data.api.collection.event.DataCollectionEvent
import net.jidb.to.base.data.api.collection.event.GeneralLootDataCollectionEvent
import net.jidb.to.base.data.api.collection.event.ItemTagDataCollectionEvent
import net.jidb.to.base.data.api.collection.event.PlacedFeatureDataCollectionEvent
import net.jidb.to.base.data.api.collection.event.RecipeDataCollectionEvent
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable

abstract class DataCollectionModule : IDataCollectionModule {

    val all: List<DataCollection<*>> field = mutableListOf()

    override fun process(collection: DataCollection<*>, event: DataCollectionEvent<*, *, *>): IDataCollectionModule.EventResult {
        val key = collection.entry
        when (event) {
            is BlockTagDataCollectionEvent -> {
                if (key.registry() != Registries.BLOCK.identifier()) return IDataCollectionModule.EventResult.PASS
                event.addResult(collection, this, generateBlockTags(collection as DataCollection<Block>, event) ?: return IDataCollectionModule.EventResult.PASS)
                return IDataCollectionModule.EventResult.SUCCESS
            }
            is ItemTagDataCollectionEvent -> {
                if (key.registry() != Registries.BLOCK.identifier() && key.registry() != Registries.ITEM.identifier()) return IDataCollectionModule.EventResult.PASS
                event.addResult(collection, this, generateItemTags(collection as DataCollection<out ItemLike>, event) ?: return IDataCollectionModule.EventResult.PASS)
                return IDataCollectionModule.EventResult.SUCCESS
            }
            is BlockLootDataCollectionEvent -> {
                if (key.registry() != Registries.BLOCK.identifier()) return IDataCollectionModule.EventResult.PASS
                event.addResult(collection, this, generateBlockLoot(collection as DataCollection<Block>, event) ?: return IDataCollectionModule.EventResult.PASS)
                return IDataCollectionModule.EventResult.SUCCESS
            }
            is RecipeDataCollectionEvent -> {
                if (key.registry() != Registries.ITEM.identifier()) return IDataCollectionModule.EventResult.PASS
                if (!generateRecipes(collection as DataCollection<Item>, event)) return IDataCollectionModule.EventResult.PASS
                return IDataCollectionModule.EventResult.SUCCESS
            }
            is GeneralLootDataCollectionEvent -> {
                event.addResult(collection, this, generateGeneralLoot(collection, event) ?: return IDataCollectionModule.EventResult.PASS)
                return IDataCollectionModule.EventResult.SUCCESS
            }
            is ConfiguredFeatureDataCollectionEvent -> {
                if (!generateConfiguredFeatures(collection, event)) return IDataCollectionModule.EventResult.PASS
                return IDataCollectionModule.EventResult.SUCCESS
            }
            is PlacedFeatureDataCollectionEvent -> {
                if (!generatePlacedFeatures(collection, event)) return IDataCollectionModule.EventResult.PASS
                return IDataCollectionModule.EventResult.SUCCESS
            }
        }
        return IDataCollectionModule.EventResult.PASS
    }

    protected open fun generateBlockTags(collection: DataCollection<Block>, event: BlockTagDataCollectionEvent): Map<TagKey<Block>, List<Block>>? = null
    protected open fun generateItemTags(collection: DataCollection<out ItemLike>, event: ItemTagDataCollectionEvent): Map<TagKey<Item>, List<Item>>? = null

    protected open fun generateBlockLoot(collection: DataCollection<Block>, event: BlockLootDataCollectionEvent): Map<Block, LootTable.Builder>? = null
    protected open fun generateRecipes(collection: DataCollection<Item>, event: RecipeDataCollectionEvent) = false

    protected open fun generateGeneralLoot(collection: DataCollection<*>, event: GeneralLootDataCollectionEvent): Map<Identifier, LootTable.Builder>? = null

    protected open fun generateConfiguredFeatures(collection: DataCollection<*>, event: ConfiguredFeatureDataCollectionEvent) = false
    protected open fun generatePlacedFeatures(collection: DataCollection<*>, event: PlacedFeatureDataCollectionEvent) = false

}
