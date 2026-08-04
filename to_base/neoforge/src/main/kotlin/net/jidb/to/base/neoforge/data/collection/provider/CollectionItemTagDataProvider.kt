package net.jidb.to.base.neoforge.data.collection.provider

import net.jidb.to.base.api.helper.RegistryHelper.resourceKey
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.ItemTagDataCollectionEvent
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ItemTagsProvider
import java.util.concurrent.CompletableFuture

/**
 * The [ItemTagsProvider] that generates the item tags described by a mod's collections.
 *
 * @param collections The collections being generated from.
 * @param output Where the generated files are written.
 * @param provider The registries the tags are built against.
 * @property modid The mod ID the files are written under.
 * @since 0.3.0
 */
class CollectionItemTagDataProvider(private val collections: Iterable<DataCollection<*>>, output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, protected val modid: String) : ItemTagsProvider(output, provider, modid) {

    override fun addTags(provider: HolderLookup.Provider) {
        val event = ItemTagDataCollectionEvent()
        val tags = event.process(collections)
        tags?.forEach { (key, items) ->
            tag(key).addAll(items.map { it.resourceKey }.toSortedSet())
        }
    }

    override fun getName() = "$modid Data Collections: ${super.name}"

}
