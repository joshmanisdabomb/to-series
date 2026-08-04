package net.jidb.to.base.neoforge.data.collection.provider

import net.jidb.to.base.api.helper.RegistryHelper.resourceKey
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.BlockTagDataCollectionEvent
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.BlockTagsProvider
import java.util.concurrent.CompletableFuture

/**
 * The [BlockTagsProvider] that generates the block tags described by a mod's collections.
 *
 * @param collections The collections being generated from.
 * @param output Where the generated files are written.
 * @param provider The registries the tags are built against.
 * @property modid The mod ID the files are written under.
 * @since 0.3.0
 */
class CollectionBlockTagDataProvider(private val collections: Iterable<DataCollection<*>>, output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, protected val modid: String) : BlockTagsProvider(output, provider, modid) {

    override fun addTags(provider: HolderLookup.Provider) {
        val event = BlockTagDataCollectionEvent()
        val tags = event.process(collections)
        tags?.forEach { (key, blocks) ->
            tag(key).addAll(blocks.map { it.resourceKey }.toSortedSet())
        }
    }

    override fun getName() = "$modid Data Collections: ${super.name}"

}
