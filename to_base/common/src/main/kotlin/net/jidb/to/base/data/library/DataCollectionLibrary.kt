package net.jidb.to.base.data.library

import net.jidb.to.base.client.data.collection.module.lang.IdentifierLanguageClientDataCollectionModule
import net.jidb.to.base.client.data.collection.module.model.block.CubeBlockModelClientDataCollectionModule
import net.jidb.to.base.client.data.collection.module.model.item.FlatItemModelClientDataCollectionModule
import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.DataCollectionDescription
import net.jidb.to.base.data.collection.module.loot.SimpleBlockLootDataCollectionModule
import net.jidb.to.base.library.Library
import net.jidb.to.base.library.SimpleLibrary
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BlockItem

open class DataCollectionLibrary(modid: String) : SimpleLibrary<DataCollectionDescription>(modid) {

    open val registries: List<Registry<*>> = listOf(BuiltInRegistries.BLOCK, BuiltInRegistries.ITEM, BuiltInRegistries.FLUID, BuiltInRegistries.ENTITY_TYPE)
    protected val _collections = mutableListOf<DataCollection<*>>()
    val collections: List<DataCollection<*>> get() = _collections

    protected open val defaultKeys: Set<ResourceKey<*>> by lazy { registries.flatMap { it.entrySet().filter { it.key.identifier().namespace == modid }.map { it.key } }.toSet() }

    protected open fun createDefaultData(description: DataCollectionDescription, vararg affects: ResourceKey<*>): DataCollectionDescription {
        description
            .addAffects(*affects)
            .addDefaultModule { if (it.`object` is BlockItem) null else IdentifierLanguageClientDataCollectionModule() }
            .addDefaultModule(::CubeBlockModelClientDataCollectionModule)
            .addDefaultModule { if (it.`object` is BlockItem) null else FlatItemModelClientDataCollectionModule() }
            .addDefaultModule(::SimpleBlockLootDataCollectionModule)
        return description
    }

    protected operator fun invoke(modify: DataCollectionDescription.(entry: Library<DataCollectionDescription, DataCollectionDescription>.LibraryEntry<DataCollectionDescription, DataCollectionDescription>) -> Unit): Library<DataCollectionDescription, DataCollectionDescription>.LibraryEntry<DataCollectionDescription, DataCollectionDescription> {
        return this.invoke(::i) { entry ->
            val affects: List<ResourceKey<*>> = defaultKeys.filter { it.identifier() == getEntryIdentifier(entry) }
            val desc = createDefaultData(DataCollectionDescription(), *affects.toTypedArray())
            desc.modify(entry)
            desc
        }
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
