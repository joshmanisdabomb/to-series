package net.jidb.to.base.data.collection.module.tag

import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.event.BlockTagDataCollectionEvent
import net.jidb.to.base.data.collection.event.ItemTagDataCollectionEvent
import net.jidb.to.base.data.collection.module.DataCollectionModule
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

open class DictTagDataCollectionModule(val ingot: TagKey<Item> = ingots, val modify: (name: String) -> String = { it }) : DataCollectionModule() {

    override fun generateBlockTags(collection: DataCollection<Block>, event: BlockTagDataCollectionEvent): Map<TagKey<Block>, List<Block>>? {
        val id = modify(collection.entry.identifier().path)
        val extra = mutableListOf<TagKey<Block>>()
        val base = if (id.endsWith("_ore")) {
            if (id.startsWith("deepslate_")) {
                extra.add(ores_deepslate)
            } else if (id.startsWith("nether_")) {
                extra.add(ores_netherrack)
            } else {
                extra.add(ores_stone)
            }
            ores
        } else if (id.endsWith("_block")) {
            storage_blocks
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
            raw_materials
        } else {
            ingot
        }

        val specific = TagKey.create(Registries.ITEM, base.location().withSuffix("/$name"))
        return listOf(base, specific).associateWith { listOf(collection.`object`.asItem()) }
    }

    companion object {
        private val ores = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores"))
        private val ores_stone = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores_in_ground/stone"))
        private val ores_deepslate = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores_in_ground/deepslate"))
        private val ores_netherrack = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores_in_ground/netherrack"))
        private val storage_blocks = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "storage_blocks"))
        private val ingots = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ingots"))
        private val raw_materials = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "raw_materials"))
        private val nuggets = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "nuggets"))
    }

}
