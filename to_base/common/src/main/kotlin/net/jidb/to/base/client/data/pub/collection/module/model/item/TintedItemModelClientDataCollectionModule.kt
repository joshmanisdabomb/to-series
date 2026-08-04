package net.jidb.to.base.client.data.pub.collection.module.model.item

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.mixin.client.ItemModelGeneratorsAccessor
import net.minecraft.client.color.item.ItemTintSource
import net.minecraft.world.item.Item

/**
 * A [ClientDataCollectionModule] generating the model of an item drawn with a coloured overlay above its base sprite, as leather armour and a potion are.
 *
 * @property tint Where the colour of the overlay is read from.
 * @property suffix The suffix of the overlay's texture, appended to the name of the item. Defaults to `_overlay`.
 * @since 0.7.0
 */
class TintedItemModelClientDataCollectionModule(val tint: ItemTintSource, val suffix: String = "_overlay") : ClientDataCollectionModule() {

    override fun generateItemModels(collection: DataCollection<Item>, event: ModelClientDataCollectionEvent): Boolean {
        (event.item as ItemModelGeneratorsAccessor).`to_base$generateItemWithTintedOverlay`(collection.`object`, suffix, tint)
        return true
    }

}
