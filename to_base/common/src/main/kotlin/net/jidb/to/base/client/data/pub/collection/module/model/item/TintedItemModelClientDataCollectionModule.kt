package net.jidb.to.base.client.data.pub.collection.module.model.item

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.mixin.client.ItemModelGeneratorsAccessor
import net.minecraft.client.color.item.ItemTintSource
import net.minecraft.world.item.Item

class TintedItemModelClientDataCollectionModule(val tint: ItemTintSource, val suffix: String = "_overlay") : ClientDataCollectionModule() {

    override fun generateItemModels(collection: DataCollection<Item>, event: ModelClientDataCollectionEvent): Boolean {
        (event.item as ItemModelGeneratorsAccessor).`to_base$generateItemWithTintedOverlay`(collection.`object`, suffix, tint)
        return true
    }

}
