package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.IResourceKeyLibrary
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.service.Services
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

open class BlockItemLibrary(modid: String, getter: () -> Map<String, () -> Block>) : SimpleLibrary<BlockItem?>(modid),
    IResourceKeyLibrary<BlockItem?, BlockItem?, Item> {

    constructor(modid: String, library: Library<*, out Block>) : this(modid, { library.entries.mapValues { it.value.getter } })

    val registry = BuiltInRegistries.ITEM
    override val registryKey get() = registry.key()

    private val blocks by lazy(getter)

    operator fun invoke(): Library<BlockItem?, BlockItem?>.LibraryEntry<BlockItem?, BlockItem?> {
        return invoke(::i) { null }.evaluateValue()
    }

    operator fun <U : BlockItem> invoke(provider: (block: (() -> Block)?, initial: Library<BlockItem?, BlockItem?>.LibraryEntry<U, U>) -> U): Library<BlockItem?, BlockItem?>.LibraryEntry<U, U> {
        return invoke(::i) { provider(blocks[it.name], it) }
    }

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