package net.jidb.to.base.client.data.api.collection.event

import net.jidb.to.base.client.data.api.model.IExtendedBlockModelGenerators
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators

/**
 * [net.jidb.to.base.data.api.collection.event.DataCollectionEvent] generating the models and blockstates of a mod.
 *
 * Unlike the other events nothing is gathered onto this one, because vanilla's generators write straight to their own output as they are called; the result of each module is only [Unit], recording that something was in fact generated.
 *
 * @property block The generator that blockstates and block models are written through.
 * @property item The generator that item models are written through.
 * @property blockEx The extra block model shapes that vanilla does not itself provide.
 * @since 0.3.0
 */
class ModelClientDataCollectionEvent(val block: BlockModelGenerators, val item: ItemModelGenerators, val blockEx: IExtendedBlockModelGenerators) : net.jidb.to.base.data.api.collection.event.DataCollectionEvent<Unit, Unit, Unit>() {

    override fun combineFromModules(results: Iterable<Unit>): Unit? {
        if (results.count() <= 0) return null
        return Unit
    }

    override fun combineFromCollections(results: Iterable<Unit>) = combineFromModules(results)

}
