package net.jidb.to.base.client.data.pub.collection.module.model.item

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.mixin.client.ItemModelGeneratorsAccessor
import net.minecraft.client.data.models.model.ModelLocationUtils
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item

/**
 * A [ClientDataCollectionModule] generating the model of a flat item, i.e. the single sprite an ordinary item is drawn as.
 * This is the default every item is given unless another model module is declared for it.
 *
 * Up to two further sprites can be drawn over the bottom one, which is how an item carrying an overlay is built.
 *
 * @property texture The texture of the bottom layer, or `null` for one named after the item. Defaults to `null`.
 * @property layers The textures drawn over the bottom layer, of which at most two are supported. Defaults to none.
 * @since 0.3.0
 */
class FlatItemModelClientDataCollectionModule(val texture: Identifier? = null, val layers: List<Identifier> = emptyList()) : ClientDataCollectionModule() {

    override fun generateItemModels(collection: DataCollection<Item>, event: ModelClientDataCollectionEvent): Boolean {
        val loc = ModelLocationUtils.getModelLocation(collection.`object`)
        val layer0 = texture ?: loc
        val materials = layers.map { Material(it) }
        when (layers.size) {
            0 -> (event.item as ItemModelGeneratorsAccessor).`to_base$generateFlatItem`(collection.`object`, ModelTemplates.FLAT_ITEM)
            1 -> ModelTemplates.TWO_LAYERED_ITEM.create(loc, TextureMapping.layered(Material(layer0), materials[0]), (event.item as ItemModelGeneratorsAccessor).`to_base$getModelOutput`())
            2 -> ModelTemplates.THREE_LAYERED_ITEM.create(loc, TextureMapping.layered(Material(layer0), materials[0], materials[1]), (event.item as ItemModelGeneratorsAccessor).`to_base$getModelOutput`())
            else -> error("${layers.size} layers not yet supported.")
        }
        return true
    }

}
