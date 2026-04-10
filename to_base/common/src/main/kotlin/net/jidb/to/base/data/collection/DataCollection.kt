package net.jidb.to.base.data.collection

import net.jidb.to.base.data.collection.event.DataCollectionEvent
import net.jidb.to.base.data.collection.module.DataCollectionModule
import net.jidb.to.base.data.collection.module.IDataCollectionModule
import net.jidb.to.base.helper.RegistryHelper
import net.minecraft.resources.ResourceKey

open class DataCollection<T : Any>(val entry: ResourceKey<T>) {

    val `object` by lazy { RegistryHelper.getResource(entry)!! }
    protected val modules = mutableListOf<DataCollectionModule>()
    protected val defaults = mutableListOf<DataCollectionModule>()

    fun add(vararg description: DataCollectionDescription) {
        description.forEach {
            it.modules.forEach { addModule(it(this) ?: return@forEach) }
            it.defaults.forEach { addDefaultModule(it(this) ?: return@forEach) }
        }
    }

    fun addModule(module: DataCollectionModule): DataCollection<T> {
        modules.add(module)
        return this
    }

    fun addDefaultModule(module: DataCollectionModule): DataCollection<T> {
        defaults.add(module)
        return this
    }

    open fun process(event: DataCollectionEvent<*, *, *>) {
        var fallback = true
        for (it in modules) {
            val result = it.process(this, event)
            if (result != IDataCollectionModule.EventResult.PASS) fallback = false
            if (result == IDataCollectionModule.EventResult.SUCCESS_BLOCK) return
        }
        if (!fallback) return
        for (it in defaults) {
            val result = it.process(this, event)
            if (result == IDataCollectionModule.EventResult.SUCCESS_BLOCK) return
        }
    }

}
