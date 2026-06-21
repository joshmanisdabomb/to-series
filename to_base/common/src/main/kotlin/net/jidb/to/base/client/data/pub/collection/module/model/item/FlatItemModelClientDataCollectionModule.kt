package net.jidb.to.base.client.data.pub.collection.module.model.item

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.mixin.client.ItemModelGeneratorsAccessor
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.world.item.Item

class FlatItemModelClientDataCollectionModule() : ClientDataCollectionModule() {

    override fun generateItemModels(collection: DataCollection<Item>, event: ModelClientDataCollectionEvent): Boolean {
        (event.item as ItemModelGeneratorsAccessor).`to_base$generateFlatItem`(collection.`object`, ModelTemplates.FLAT_ITEM)
        return true
    }

}