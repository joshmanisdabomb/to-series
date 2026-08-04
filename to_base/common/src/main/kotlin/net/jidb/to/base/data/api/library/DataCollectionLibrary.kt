package net.jidb.to.base.data.api.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.data.pub.collection.module.lang.IdentifierLanguageClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.model.block.CubeBlockModelClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.model.item.FlatItemModelClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.DataCollectionDescription
import net.jidb.to.base.data.pub.collection.module.loot.SimpleBlockLootDataCollectionModule
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BlockItem

/**
 * [SimpleLibrary] implementation that declares how each piece of a mod's content is generated, and turns those declarations into the collections the generators read.
 *
 * The point of the library is that nothing has to be declared for content that wants the ordinary treatment: every registered object of the mod gets a collection whether it was named here or not, carrying the default modules from [createDefaultData], and an entry is only written where something about a piece of content differs from that.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.3.0
 */
open class DataCollectionLibrary(modid: String) : SimpleLibrary<DataCollectionDescription>(modid) {

    /**
     * The registries whose entries are generated for. Defaults to blocks, items, fluids and entity types.
     *
     * @since 0.3.0
     */
    open val registries: List<Registry<*>> = listOf(BuiltInRegistries.BLOCK, BuiltInRegistries.ITEM, BuiltInRegistries.FLUID, BuiltInRegistries.ENTITY_TYPE)

    /**
     * The backing property of [collections].
     *
     * @since 0.3.0
     */
    protected val _collections = mutableListOf<DataCollection<*>>()

    /**
     * A collection per registry entry of this mod, built once the library has been built.
     *
     * @since 0.3.0
     */
    val collections: List<DataCollection<*>> get() = _collections

    /**
     * Every entry of [registries] belonging to this mod, which is what a collection is built for.
     *
     * @since 0.3.0
     */
    protected open val defaultKeys: Set<ResourceKey<*>> by lazy { registries.flatMap { it.entrySet().filter { it.key.identifier().namespace == modid }.map { it.key } }.toSet() }

    /**
     * Fills a description with the modules every piece of content is given unless it says otherwise: a translation, a cube block model, a flat item model and a loot table dropping itself.
     * A block item is skipped for the language and item model modules, as it takes both from the block it belongs to.
     *
     * @param description The description to add the default modules to.
     * @param affects The registry entries the description applies to.
     * @return The description, for further chaining.
     * @since 0.3.0
     */
    protected open fun createDefaultData(description: DataCollectionDescription, vararg affects: ResourceKey<*>): DataCollectionDescription {
        description
            .addAffects(*affects)
            .addDefaultModule { if (it.`object` is BlockItem) null else IdentifierLanguageClientDataCollectionModule() }
            .addDefaultModule(::CubeBlockModelClientDataCollectionModule)
            .addDefaultModule { if (it.`object` is BlockItem) null else FlatItemModelClientDataCollectionModule() }
            .addDefaultModule(::SimpleBlockLootDataCollectionModule)
        return description
    }

    /**
     * Declares how the content named after this entry is generated, starting from the defaults and letting the given block change them.
     *
     * @param modify A function adjusting the description, given the entry being declared.
     * @return The [Library.LibraryEntry] for the declared property.
     * @since 0.3.0
     */
    protected operator fun invoke(modify: DataCollectionDescription.(entry: Library<DataCollectionDescription, DataCollectionDescription>.LibraryEntry<DataCollectionDescription, DataCollectionDescription>) -> Unit) = this.invoke(::i) { entry ->
        val affects: List<ResourceKey<*>> = defaultKeys.filter { it.identifier() == getEntryIdentifier(entry) }
        val desc = createDefaultData(DataCollectionDescription(), *affects.toTypedArray())
        desc.modify(entry)
        desc
    }

    override fun afterBuild() {
        for (key in defaultKeys) {
            var matches = values.filter { it.affects.any { it(key) } }
            if (matches.isEmpty()) {
                matches = listOf(createDefaultData(DataCollectionDescription(), key))
            }

            val collection = DataCollection(key)
            collection.add(*matches.toTypedArray())
            _collections.add(collection)
        }
    }

}
