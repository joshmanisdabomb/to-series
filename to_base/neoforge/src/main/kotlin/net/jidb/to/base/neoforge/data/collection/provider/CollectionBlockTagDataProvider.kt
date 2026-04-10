package net.jidb.to.base.neoforge.data.collection.provider

import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.event.BlockTagDataCollectionEvent
import net.jidb.to.base.helper.IdentifierHelper.identifier
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.BlockTagsProvider
import java.util.concurrent.CompletableFuture

class CollectionBlockTagDataProvider(private val collections: Iterable<DataCollection<*>>, output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, protected val modid: String) : BlockTagsProvider(output, provider, modid) {

    override fun addTags(provider: HolderLookup.Provider) {
        val event = BlockTagDataCollectionEvent()
        val tags = event.process(collections)
        tags?.forEach { (key, blocks) ->
            tag(key).addAll(blocks.toSortedSet(Comparator.comparing { it.identifier }))
        }
    }

    override fun getName() = "$modid Data Collections: ${super.name}"

}