package net.jidb.to.base.library

import net.jidb.to.base.service.Services
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

open class BlockItemLibrary(modid: String, getter: () -> Map<String, () -> Block>) : SimpleLibrary<BlockItem?>(modid), IResourceKeyLibrary<BlockItem?, BlockItem?, Item> {

    constructor(modid: String, library: Library<*, out Block>) : this(modid, { library.entries.mapValues { it.value.getter } })

    val registry = BuiltInRegistries.ITEM
    override val registryKey get() = registry.key()

    private val blocks by lazy(getter)

    override fun afterBuild() {
        for ((name, block) in blocks) {
            val entry = this.entries[name]
            if (entry?.evaluated == true && entry.value == null) {
                continue
            }
            val blockitem = entry?.getter ?: createDefault(block, name)
            Services.register(registry, Identifier.fromNamespaceAndPath(modid, name)) { blockitem()!! }
        }
    }

    open fun createDefault(block: () -> Block, name: String): () -> BlockItem {
        val key = ResourceKey.create(registry.key(), Identifier.fromNamespaceAndPath(modid, name))
        return { BlockItem(block(), Item.Properties().setId(key).useBlockDescriptionPrefix()) }
    }

}