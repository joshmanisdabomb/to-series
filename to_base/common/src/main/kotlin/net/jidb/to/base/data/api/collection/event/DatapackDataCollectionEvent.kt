package net.jidb.to.base.data.api.collection.event

import com.mojang.serialization.Lifecycle
import net.minecraft.core.Registry
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import kotlin.jvm.optionals.getOrNull

abstract class DatapackDataCollectionEvent<T : Any>(protected val context: BootstrapContext<T>) : DataCollectionEvent<Unit, Unit, Unit>() {

    fun add(key: ResourceKey<T>, value: T, lifecycle: Lifecycle = Lifecycle.stable()) = context.register(key, value, lifecycle)

    fun <S : Any> lookupRegistry(key: ResourceKey<out Registry<S>>) = context.lookup(key)
    fun <S : Any> lookup(key: ResourceKey<S>) = lookupRegistry(key.registryKey()).get(key).getOrNull()
    fun <S : Any> lookup(tag: TagKey<S>) = lookupRegistry(tag.registry).get(tag)

    override fun combineFromModules(results: Iterable<Unit>): Unit? {
        if (results.count() <= 0) return null
        return Unit
    }

    override fun combineFromCollections(results: Iterable<Unit>) = combineFromModules(results)

}
