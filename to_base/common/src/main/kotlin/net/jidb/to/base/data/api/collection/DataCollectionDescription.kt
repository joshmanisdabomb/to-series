package net.jidb.to.base.data.api.collection

import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.resources.ResourceKey

open class DataCollectionDescription(vararg affects: (key: ResourceKey<*>) -> Boolean) {

    val affects: Set<(key: ResourceKey<*>) -> Boolean> field = affects.toMutableSet()
    val modules: Set<(collection: DataCollection<*>) -> DataCollectionModule?> field = mutableSetOf<(collection: DataCollection<*>) -> DataCollectionModule?>()
    val defaults: Set<(collection: DataCollection<*>) -> DataCollectionModule?> field = mutableSetOf<(collection: DataCollection<*>) -> DataCollectionModule?>()

    fun addAffects(vararg affect: ResourceKey<*>, clear: Boolean = false) = addAffects(clear) { it in affect }
    fun addAffects(clear: Boolean = false, affect: (key: ResourceKey<*>) -> Boolean): DataCollectionDescription {
        if (clear) clearAffects()
        affects.add(affect)
        return this
    }

    fun clearAffects(): DataCollectionDescription {
        affects.clear()
        return this
    }

    fun addModule(module: () -> DataCollectionModule?, clear: Boolean = false) = addModule(clear) { _ -> module() }
    fun addModule(clear: Boolean = false, module: (collection: DataCollection<*>) -> DataCollectionModule?): DataCollectionDescription {
        if (clear) clearModules()
        modules.add(module)
        return this
    }

    fun clearModules(): DataCollectionDescription {
        modules.clear()
        return this
    }

    fun addDefaultModule(module: () -> DataCollectionModule?, clear: Boolean = false) = addDefaultModule(clear) { _ -> module() }
    fun addDefaultModule(clear: Boolean = false, module: (collection: DataCollection<*>) -> DataCollectionModule?): DataCollectionDescription {
        if (clear) clearDefaultModules()
        defaults.add(module)
        return this
    }

    fun clearDefaultModules(): DataCollectionDescription {
        defaults.clear()
        return this
    }

}
