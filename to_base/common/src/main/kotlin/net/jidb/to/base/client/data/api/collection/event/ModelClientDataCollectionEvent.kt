package net.jidb.to.base.client.data.api.collection.event

import net.jidb.to.base.client.data.api.model.IExtendedBlockModelGenerators
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators

class ModelClientDataCollectionEvent(val block: BlockModelGenerators, val item: ItemModelGenerators, val blockEx: IExtendedBlockModelGenerators) : net.jidb.to.base.data.api.collection.event.DataCollectionEvent<Unit, Unit, Unit>() {

    override fun combineFromModules(results: Iterable<Unit>): Unit? {
        if (results.count() <= 0) return null
        return Unit
    }

    override fun combineFromCollections(results: Iterable<Unit>) = combineFromModules(results)

}
