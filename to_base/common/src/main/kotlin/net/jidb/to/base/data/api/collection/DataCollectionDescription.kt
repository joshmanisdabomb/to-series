package net.jidb.to.base.data.api.collection

import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.resources.ResourceKey

open class DataCollectionDescription(vararg affects: (key: ResourceKey<*>) -> Boolean) {

    protected open val _affects = affects.toMutableSet()
    val affects: Set<(key: ResourceKey<*>) -> Boolean> get() = _affects
    protected open val _modules = mutableSetOf<(collection: DataCollection<*>) -> DataCollectionModule?>()
    val modules: Set<(collection: DataCollection<*>) -> DataCollectionModule?> get() = _modules
    protected open val _defaults = mutableSetOf<(collection: DataCollection<*>) -> DataCollectionModule?>()
    val defaults: Set<(collection: DataCollection<*>) -> DataCollectionModule?> get() = _defaults

    fun addAffects(vararg affect: ResourceKey<*>, clear: Boolean = false) = addAffects(clear) { it in affect }
    fun addAffects(clear: Boolean = false, affect: (key: ResourceKey<*>) -> Boolean): DataCollectionDescription {
        if (clear) clearAffects()
        _affects.add(affect)
        return this
    }

    fun clearAffects(): DataCollectionDescription {
        _affects.clear()
        return this
    }

    fun addModule(module: () -> DataCollectionModule?, clear: Boolean = false) = addModule(clear) { it -> module() }
    fun addModule(clear: Boolean = false, module: (collection: DataCollection<*>) -> DataCollectionModule?): DataCollectionDescription {
        if (clear) clearModules()
        _modules.add(module)
        return this
    }

    fun clearModules(): DataCollectionDescription {
        _modules.clear()
        return this
    }

    fun addDefaultModule(module: () -> DataCollectionModule?, clear: Boolean = false) = addDefaultModule(clear) { it -> module() }
    fun addDefaultModule(clear: Boolean = false, module: (collection: DataCollection<*>) -> DataCollectionModule?): DataCollectionDescription {
        if (clear) clearDefaultModules()
        _defaults.add(module)
        return this
    }

    fun clearDefaultModules(): DataCollectionDescription {
        _defaults.clear()
        return this
    }

}