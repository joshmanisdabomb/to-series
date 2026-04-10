package net.jidb.to.base.neoforge.data.collection.provider

import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.event.ItemTagDataCollectionEvent
import net.jidb.to.base.helper.IdentifierHelper.identifier
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ItemTagsProvider
import java.util.concurrent.CompletableFuture

class CollectionItemTagDataProvider(private val collections: Iterable<DataCollection<*>>, output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, protected val modid: String) : ItemTagsProvider(output, provider, modid) {

    override fun addTags(provider: HolderLookup.Provider) {
        val event = ItemTagDataCollectionEvent()
        val tags = event.process(collections)
        tags?.forEach { (key, items) ->
            tag(key).addAll(items.toSortedSet(Comparator.comparing { it.identifier }))
        }
    }

    override fun getName() = "$modid Data Collections: ${super.name}"

}