package net.jidb.to.base.client.data.api.collection.module

import net.jidb.to.base.client.data.api.collection.event.LangClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.DataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.jidb.to.base.data.api.collection.module.IDataCollectionModule
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

/**
 * A [DataCollectionModule] for the data that only the client needs, i.e. translations, models and blockstates.
 * It is split from the server-side modules because the generators it writes through only exist on the client.
 *
 * A subclass overrides whichever of the generate functions describes the kind of data it produces, and anything it leaves alone is passed on to the next module.
 *
 * @since 0.3.0
 */
abstract class ClientDataCollectionModule : DataCollectionModule() {

    override fun process(collection: DataCollection<*>, event: DataCollectionEvent<*, *, *>): IDataCollectionModule.EventResult {
        val key = collection.entry
        when (event) {
            is LangClientDataCollectionEvent -> {
                event.addResult(collection, this, generateLang(collection, event) ?: return IDataCollectionModule.EventResult.PASS)
                return IDataCollectionModule.EventResult.SUCCESS
            }
            is ModelClientDataCollectionEvent -> {
                when (key.registry()) {
                    Registries.BLOCK.identifier() -> {
                        if (!generateBlockModels(collection as DataCollection<Block>, event)) return IDataCollectionModule.EventResult.PASS
                        event.addResult(collection, this, Unit)
                        return IDataCollectionModule.EventResult.SUCCESS_BLOCK
                    }
                    Registries.ITEM.identifier() -> {
                        if (!generateItemModels(collection as DataCollection<Item>, event)) return IDataCollectionModule.EventResult.PASS
                        event.addResult(collection, this, Unit)
                        return IDataCollectionModule.EventResult.SUCCESS_BLOCK
                    }
                }
            }
        }
        return super.process(collection, event)
    }

    /**
     * The translations this module generates for an entry, keyed by locale and then by translation key.
     *
     * @param collection The collection being generated for.
     * @param event The event being generated for.
     * @return The translations, or `null` where this module generates none.
     * @since 0.3.0
     */
    protected open fun generateLang(collection: DataCollection<*>, event: LangClientDataCollectionEvent): Map<String, Map<String, Component>>? = null

    /**
     * Writes the blockstate and block models of a block through the generators on the event.
     * Defaults to [generateModels], so a module that treats blocks and items alike need only override that.
     *
     * @param collection The collection being generated for.
     * @param event The event being generated for.
     * @return Returns `true` if anything was generated, `false` to leave the block to the next module.
     * @since 0.3.0
     */
    protected open fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean = generateModels(collection, event)

    /**
     * Writes the models of an item through the generators on the event.
     * Defaults to [generateModels], so a module that treats blocks and items alike need only override that.
     *
     * @param collection The collection being generated for.
     * @param event The event being generated for.
     * @return Returns `true` if anything was generated, `false` to leave the item to the next module.
     * @since 0.3.0
     */
    protected open fun generateItemModels(collection: DataCollection<Item>, event: ModelClientDataCollectionEvent): Boolean = generateModels(collection, event)

    /**
     * Writes the models of a block or an item through the generators on the event, where the same treatment suits both.
     *
     * @param collection The collection being generated for.
     * @param event The event being generated for.
     * @return Returns `true` if anything was generated, `false` to leave the entry to the next module. Defaults to `false`.
     * @since 0.3.0
     */
    protected open fun generateModels(collection: DataCollection<out ItemLike>, event: ModelClientDataCollectionEvent): Boolean = false

}
