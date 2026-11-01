package net.jidb.to.base.client.data.pub.collection.module.model.item

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.mixin.client.ItemModelGeneratorsAccessor
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.world.item.Item

/**
 * A [ClientDataCollectionModule] that uses an item's name to determine its equipment type, and then gives it its corresponding model extending a typical vanilla equipment type.
 *
 * @since 1.1.0
 */
class EquipmentModelClientDataCollectionModule(val modify: (name: String) -> String = { it }) : ClientDataCollectionModule() {

    override fun generateItemModels(collection: DataCollection<Item>, event: ModelClientDataCollectionEvent): Boolean {
        val id = modify(collection.entry.identifier().path)

        if (id.endsWith("spear")) {
            (event.item as ItemModelGeneratorsAccessor).`to_base$generateSpear`(collection.`object`)
            return true
        } else if (listOf("sword", "pickaxe", "shovel", "axe", "hoe").any { id.endsWith("_$it") }) {
            (event.item as ItemModelGeneratorsAccessor).`to_base$generateFlatItem`(collection.`object`, ModelTemplates.FLAT_HANDHELD_ITEM)
            return true
        }

        return false
    }

}
