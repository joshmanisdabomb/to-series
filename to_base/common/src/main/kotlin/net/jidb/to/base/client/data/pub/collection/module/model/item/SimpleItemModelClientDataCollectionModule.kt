package net.jidb.to.base.client.data.pub.collection.module.model.item

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.mixin.client.ItemModelGeneratorsAccessor
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.world.item.Item

/**
 * A [ClientDataCollectionModule] generating the model of an item from a named model template and a texture mapping filling it, for an item that is drawn as neither a flat sprite nor a block.
 *
 * @property template The model the item is drawn from.
 * @property textures The textures filling that model's slots.
 * @since 0.6.0
 */
class SimpleItemModelClientDataCollectionModule(val template: ModelTemplate, val textures: TextureMapping) : ClientDataCollectionModule() {

    override fun generateItemModels(collection: DataCollection<Item>, event: ModelClientDataCollectionEvent): Boolean {
        val accessor = event.item as ItemModelGeneratorsAccessor
        val model = template.create(
            collection.`object`,
            textures,
            accessor.`to_base$getModelOutput`()
        )
        accessor.`to_base$getItemModelOutput`().accept(collection.`object`, ItemModelUtils.plainModel(model))
        return true
    }

}
