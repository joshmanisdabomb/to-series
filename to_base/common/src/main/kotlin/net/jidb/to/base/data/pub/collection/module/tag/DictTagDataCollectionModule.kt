package net.jidb.to.base.data.pub.collection.module.tag

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.BlockTagDataCollectionEvent
import net.jidb.to.base.data.api.collection.event.ItemTagDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

/**
 * A [DataCollectionModule] putting a block or item into the common `c:` tags other mods look it up by, worked out from the name it was registered under.
 *
 * A name ending in `_ore` is an ore, one ending in `_block` a storage block, one ending in `_nugget` a nugget and one starting with `raw_` a raw material; anything else is taken to be an ingot.
 * Each is put into both the broad tag and the material-specific one under it, i.e. `c:ingots` and `c:ingots/uranium`, and an ore is additionally tagged by the stone it is found in.
 *
 * @property ingot The tag to use for a name that matches none of the recognised suffixes. Defaults to `c:ingots`.
 * @property modify A function adjusting the registered name before it is read, for content whose name does not follow the convention. Defaults to leaving it as it is.
 * @since 0.3.0
 */
open class DictTagDataCollectionModule(val ingot: TagKey<Item> = ingots, val modify: (name: String) -> String = { it }) : DataCollectionModule() {

    override fun generateBlockTags(collection: DataCollection<Block>, event: BlockTagDataCollectionEvent): Map<TagKey<Block>, List<Block>>? {
        val id = modify(collection.entry.identifier().path)
        val extra = mutableListOf<TagKey<Block>>()
        val base = if (id.endsWith("_ore")) {
            if (id.startsWith("deepslate_")) {
                extra.add(oresDeepslate)
            } else if (id.startsWith("nether_")) {
                extra.add(oresNetherrack)
            } else {
                extra.add(oresStone)
            }
            ores
        } else if (id.endsWith("_block")) {
            storageBlocks
        } else {
            null
        }

        if (base == null) return emptyMap()
        val name = id.replace("deepslate_", "").substringBeforeLast('_')
        val specific = TagKey.create(Registries.BLOCK, base.location().withSuffix("/$name"))
        return listOf(base, specific, *extra.toTypedArray()).associateWith { listOf(collection.`object`) }
    }

    override fun generateItemTags(collection: DataCollection<out ItemLike>, event: ItemTagDataCollectionEvent): Map<TagKey<Item>, List<Item>>? {
        if (collection.`object`.asItem() is BlockItem) return emptyMap()

        val id = modify(collection.entry.identifier().path)
        var name = id.substringBeforeLast('_')
        val base = if (id.endsWith("_nugget")) {
            nuggets
        } else if (id.startsWith("raw_")) {
            name = id.substringAfter("raw_")
            rawMaterials
        } else {
            ingot
        }

        val specific = TagKey.create(Registries.ITEM, base.location().withSuffix("/$name"))
        return listOf(base, specific).associateWith { listOf(collection.`object`.asItem()) }
    }

    companion object {

        /**
         * The common tag holding every ore.
         *
         * @since 0.3.0
         */
        private val ores = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores"))

        /**
         * The common tag holding every ore found in stone.
         *
         * @since 0.3.0
         */
        private val oresStone = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores_in_ground/stone"))

        /**
         * The common tag holding every ore found in deepslate.
         *
         * @since 0.3.0
         */
        private val oresDeepslate = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores_in_ground/deepslate"))

        /**
         * The common tag holding every ore found in netherrack.
         *
         * @since 0.3.0
         */
        private val oresNetherrack = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores_in_ground/netherrack"))

        /**
         * The common tag holding every block that a material is compacted into.
         *
         * @since 0.3.0
         */
        private val storageBlocks = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "storage_blocks"))

        /**
         * The common tag holding every ingot, which is the default for a name matching no other suffix.
         *
         * @since 0.3.0
         */
        private val ingots = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ingots"))

        /**
         * The common tag holding every raw material, i.e. what an ore drops before it is smelted.
         *
         * @since 0.3.0
         */
        private val rawMaterials = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "raw_materials"))

        /**
         * The common tag holding every nugget.
         *
         * @since 0.3.0
         */
        private val nuggets = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "nuggets"))

    }

}
