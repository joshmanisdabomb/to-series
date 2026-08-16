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

    protected open fun generateLang(collection: DataCollection<*>, event: LangClientDataCollectionEvent): Map<String, Map<String, Component>>? = null

    protected open fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean = generateModels(collection, event)
    protected open fun generateItemModels(collection: DataCollection<Item>, event: ModelClientDataCollectionEvent): Boolean = generateModels(collection, event)
    protected open fun generateModels(collection: DataCollection<out ItemLike>, event: ModelClientDataCollectionEvent): Boolean = false

}
