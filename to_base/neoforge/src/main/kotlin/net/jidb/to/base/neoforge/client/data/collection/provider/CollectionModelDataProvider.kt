package net.jidb.to.base.neoforge.client.data.collection.provider

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.neoforge.client.data.model.ExtendedBlockModelGenerators
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.data.PackOutput

/**
 * The [ModelProvider] that generates the models and blockstates described by a mod's collections.
 *
 * @param collections The collections being generated from.
 * @param output Where the generated files are written.
 * @property modid The mod ID the files are written under.
 * @since 0.3.0
 */
class CollectionModelDataProvider(private val collections: Iterable<DataCollection<*>>, output: PackOutput, protected val modid: String) : ModelProvider(output, modid) {

    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        val extended = ExtendedBlockModelGenerators(blockModels)
        val event = ModelClientDataCollectionEvent(blockModels, itemModels, extended)
        event.process(collections)
    }

    override fun getName() = "$modid Data Collections: ${super.name}"

}
