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

/**
 * The base class for a data generation module, which turns one registry entry into some of the data generated for it.
 * [process] is implemented here to work out which kind of data an event is asking for, check the entry belongs to a registry that kind applies to, and call the matching generate function; a subclass overrides only the generate functions it has something to say for.
 *
 * A generate function returning `null` or `false` is how a module passes, which leaves the default modules of the collection free to answer instead.
 *
 * @see net.jidb.to.base.data.api.collection.DataCollectionDescription
 * @since 0.3.0
 */
abstract class DataCollectionModule : IDataCollectionModule {

    /**
     * Every collection this module has been added to, which lets a module generate something spanning all of them, such as a tag holding each entry it covers.
     *
     * @since 0.3.0
     */
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

    /**
     * Generates the block tags this module puts the collection's block into.
     *
     * @param collection The collection being generated for.
     * @param event The event being generated for.
     * @return The blocks to add to each tag, or `null` to pass.
     * @since 0.3.0
     */
    protected open fun generateBlockTags(collection: DataCollection<Block>, event: BlockTagDataCollectionEvent): Map<TagKey<Block>, List<Block>>? = null

    /**
     * Generates the item tags this module puts the collection's item into.
     * A block is offered here as well as an item, so that a tag can be declared against a block and applied to the item it drops as.
     *
     * @param collection The collection being generated for.
     * @param event The event being generated for.
     * @return The items to add to each tag, or `null` to pass.
     * @since 0.3.0
     */
    protected open fun generateItemTags(collection: DataCollection<out ItemLike>, event: ItemTagDataCollectionEvent): Map<TagKey<Item>, List<Item>>? = null

    /**
     * Generates the loot table for the collection's block, i.e. what it drops when broken.
     *
     * @param collection The collection being generated for.
     * @param event The event being generated for.
     * @return The loot table to generate for each block, or `null` to pass.
     * @since 0.3.0
     */
    protected open fun generateBlockLoot(collection: DataCollection<Block>, event: BlockLootDataCollectionEvent): Map<Block, LootTable.Builder>? = null

    /**
     * Generates the recipes producing or consuming the collection's item.
     * Recipes are written straight onto the event's output rather than returned, as one item may produce any number of them.
     *
     * @param collection The collection being generated for.
     * @param event The event being generated for.
     * @return Returns `true` if any recipe was generated, `false` to pass.
     * @since 0.3.0
     */
    protected open fun generateRecipes(collection: DataCollection<Item>, event: RecipeDataCollectionEvent) = false

    /**
     * Generates loot tables that are not a block's own drops, such as a chest table the collection's item appears in.
     *
     * @param collection The collection being generated for.
     * @param event The event being generated for.
     * @return The loot table to generate under each identifier, or `null` to pass.
     * @since 0.8.0
     */
    protected open fun generateGeneralLoot(collection: DataCollection<*>, event: GeneralLootDataCollectionEvent): Map<Identifier, LootTable.Builder>? = null

    /**
     * Generates the configured features involving the collection's entry, such as the shape of an ore's own vein.
     * Features are written straight onto the event's output rather than returned, as one entry may produce any number of them.
     *
     * @param collection The collection being generated for.
     * @param event The event being generated for.
     * @return Returns `true` if any configured feature was generated, `false` to pass.
     * @since 0.3.0
     */
    protected open fun generateConfiguredFeatures(collection: DataCollection<*>, event: ConfiguredFeatureDataCollectionEvent) = false

    /**
     * Generates the placed features involving the collection's entry, i.e. where and how often its configured features appear.
     * Features are written straight onto the event's output rather than returned, as one entry may produce any number of them.
     *
     * @param collection The collection being generated for.
     * @param event The event being generated for.
     * @return Returns `true` if any placed feature was generated, `false` to pass.
     * @since 0.3.0
     */
    protected open fun generatePlacedFeatures(collection: DataCollection<*>, event: PlacedFeatureDataCollectionEvent) = false

}
