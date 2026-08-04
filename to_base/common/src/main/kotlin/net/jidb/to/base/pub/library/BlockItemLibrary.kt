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

/**
 * [SimpleLibrary] implementation that registers a [BlockItem] to [BuiltInRegistries.ITEM] for each block of a [BlockLibrary], and provides access to that content in one place.
 * Every block gets an item without the library having to name it: a block that needs nothing unusual is not declared here at all and still receives one from [createDefault], a block that needs a custom item declares it, and a block that should have no item at all declares the no-argument [invoke].
 *
 * @param modid The mod ID associated with the library.
 * @param getter A function supplying the blocks to create items for, indexed by the name each was registered under.
 * @since 0.0.3
 */
open class BlockItemLibrary(modid: String, getter: () -> Map<String, () -> Block>) : SimpleLibrary<BlockItem?>(modid),
    IResourceKeyLibrary<BlockItem?, BlockItem?, Item> {

    /**
     * Creates a library for the blocks of an existing [Library], which is how a [BlockLibrary] is normally paired with its items.
     *
     * @param modid The mod ID associated with the library.
     * @param library The library whose entries the block items are created for.
     * @since 0.0.3
     */
    constructor(modid: String, library: Library<*, out Block>) : this(modid, { library.entries.mapValues { it.value.getter } })

    /**
     * The [net.minecraft.core.Registry] this library registers its block items into.
     *
     * @since 0.0.3
     */
    val registry = BuiltInRegistries.ITEM

    override val registryKey get() = registry.key()

    /**
     * The blocks to create items for, resolved lazily so that the block library has been built by the time they are read.
     *
     * @since 0.0.3
     */
    private val blocks by lazy(getter)

    /**
     * Declares that the block of this entry has no item at all, by registering an entry whose value is `null`.
     * The value is evaluated at build time so that [afterBuild] can tell a deliberate absence from an entry that has simply not been read yet.
     *
     * @return The [Library.LibraryEntry] for the declared property, whose value is always `null`.
     * @since 0.6.0
     */
    operator fun invoke() = invoke(::i) { null }.evaluateValue()

    /**
     * Declares a custom [BlockItem] for the block registered under the same name as this entry.
     *
     * @param U The type of the block item being declared.
     * @param provider A function that builds the block item, given a supplier of the block of the same name (or `null` where there is none) and the entry being built.
     * @return The [Library.LibraryEntry] for the declared property.
     * @since 0.6.0
     */
    operator fun <U : BlockItem> invoke(provider: (block: (() -> Block)?, initial: Library<BlockItem?, BlockItem?>.LibraryEntry<U, U>) -> U): Library<BlockItem?, BlockItem?>.LibraryEntry<U, U> = invoke(::i) { provider(blocks[it.name], it) }

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

    /**
     * Builds the [BlockItem] used for a block that did not declare one of its own.
     * Can be overridden by subclasses that want a different default, such as an item with a larger stack size or a different description prefix.
     *
     * @param block A supplier of the block the item is for.
     * @param name The name the block was registered under, which the item is registered under too.
     * @return A supplier of the default [BlockItem] for the given block.
     * @since 0.0.3
     */
    open fun createDefault(block: () -> Block, name: String): () -> BlockItem {
        val key = ResourceKey.create(registry.key(), Identifier.fromNamespaceAndPath(modid, name))
        return { BlockItem(block(), Item.Properties().setId(key).useBlockDescriptionPrefix()) }
    }

}
