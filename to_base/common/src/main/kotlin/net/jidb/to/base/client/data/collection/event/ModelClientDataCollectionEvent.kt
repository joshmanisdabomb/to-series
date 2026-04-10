package net.jidb.to.base.client.data.collection.event

import net.jidb.to.base.client.data.model.IExtendedBlockModelGenerators
import net.jidb.to.base.data.collection.event.DataCollectionEvent
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators

class ModelClientDataCollectionEvent(val block: BlockModelGenerators, val item: ItemModelGenerators, val blockEx: IExtendedBlockModelGenerators) : DataCollectionEvent<Unit, Unit, Unit>() {

    override fun combineFromModules(results: Iterable<Unit>): Unit? {
        if (results.count() <= 0) return null
        return Unit
    }

    override fun combineFromCollections(results: Iterable<Unit>) = combineFromModules(results)

}