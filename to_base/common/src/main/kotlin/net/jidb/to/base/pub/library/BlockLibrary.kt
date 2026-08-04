package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.LibraryTagList
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.base.api.properties.ExtendedBlockProperties
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Block

/**
 * [SimpleRegistryLibrary] implementation that registers [Block] content to [BuiltInRegistries.BLOCK], and provides access to that content in one place.
 * Alongside the blocks themselves it collects the [ExtendedBlockProperties] declared against them, which are the properties that vanilla's own `Block.Properties` has no room for.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.1.0
 */
open class BlockLibrary(modid: String) : SimpleRegistryLibrary<Block>(modid) {

    override val registry = BuiltInRegistries.BLOCK

    /**
     * The [ExtendedBlockProperties] declared against the blocks of this library, indexed by the entry that declared them.
     *
     * @since 0.1.0
     */
    val properties = ExtendedBlockPropertiesList()

    /**
     * The [LibraryTagList] that holds the [ExtendedBlockProperties] of each block in the outer [BlockLibrary].
     * A block may carry more than one set of properties, so each entry maps to a list of them.
     *
     * @since 0.1.0
     */
    inner class ExtendedBlockPropertiesList : LibraryTagList<Block, ExtendedBlockProperties>() {

        /**
         * Applies every set of properties collected in this list to the block that declared it.
         * This runs once the library has been built, so that each entry has a block to apply them to.
         *
         * @since 0.1.0
         */
        fun build() {
            entries.forEach { (key, entry) ->
                getEntryTags(this, entry).forEach { it.build(entry.value) }
            }
        }

    }

}
