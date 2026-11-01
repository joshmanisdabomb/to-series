package net.jidb.to.base.data.pub.collection.module.tag

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.ItemTagDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

/**
 * A [DataCollectionModule] that uses an item's name to determine its equipment type, and then assigns it to the corresponding vanilla tag.
 *
 * @property modify A function adjusting the registered name before it is read, for content whose name does not follow the convention. Defaults to leaving it as it is.
 * @since 1.1.0
 */
open class EquipmentTagDataCollectionModule(val modify: (name: String) -> String = { it }) : DataCollectionModule() {

    override fun generateItemTags(collection: DataCollection<out ItemLike>, event: ItemTagDataCollectionEvent): Map<TagKey<Item>, List<Item>>? {
        val id = modify(collection.entry.identifier().path)
        val tags = if (id.endsWith("_sword")) listOf(ItemTags.SWORDS)
        else if (id.endsWith("_pickaxe")) listOf(ItemTags.PICKAXES)
        else if (id.endsWith("_shovel")) listOf(ItemTags.SHOVELS)
        else if (id.endsWith("_axe")) listOf(ItemTags.AXES)
        else if (id.endsWith("_hoe")) listOf(ItemTags.HOES)
        else if (id.endsWith("_spear")) listOf(ItemTags.SPEARS)
        else if (id.endsWith("_helmet")) listOf(ItemTags.HEAD_ARMOR)
        else if (id.endsWith("_chestplate")) listOf(ItemTags.CHEST_ARMOR)
        else if (id.endsWith("_leggings")) listOf(ItemTags.LEG_ARMOR)
        else if (id.endsWith("_boots")) listOf(ItemTags.FOOT_ARMOR)
        else if (id.endsWith("_horse_armor")) listOf(horse_armors)
        else if (id.endsWith("_nautilus_armor")) listOf(nautilus_armors)
        else emptyList()

        return tags.associateWith { listOf(collection.`object`.asItem()) }
    }

    companion object {

        /**
         * The common tag in Neoforge and Fabric holding every horse armor.
         *
         * @since 1.1.0
         */
        private val horse_armors = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "armors/horse"))

        /**
         * The common tag in Neoforge and Fabric holding every nautilus armor.
         *
         * @since 1.1.0
         */
        private val nautilus_armors = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "armors/nautilus"))

    }

}
