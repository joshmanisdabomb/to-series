package net.jidb.to.base.data.api.collection.event

import com.mojang.serialization.Lifecycle
import net.minecraft.core.Registry
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import kotlin.jvm.optionals.getOrNull

/**
 * The [DataCollectionEvent] generating data pack registry content, such as a configured feature or a damage type.
 * Vanilla builds these through a bootstrap context rather than by returning them, so a module registers each entry as it goes and nothing is merged; the result type is [Unit] and only records that something was generated at all.
 *
 * @param T The type of data pack content being generated.
 * @property context The bootstrap context entries are registered into and looked up through.
 * @since 0.3.0
 */
abstract class DatapackDataCollectionEvent<T : Any>(protected val context: BootstrapContext<T>) : DataCollectionEvent<Unit, Unit, Unit>() {

    /**
     * Registers a generated entry into the data pack.
     *
     * @param key The key to register the entry under.
     * @param value The entry to register.
     * @param lifecycle The lifecycle to register the entry with. Defaults to stable.
     * @return The holder of the registered entry.
     * @since 0.3.0
     */
    fun add(key: ResourceKey<T>, value: T, lifecycle: Lifecycle = Lifecycle.stable()) = context.register(key, value, lifecycle)

    /**
     * Looks up another data pack registry, which is how a generated entry refers to one that another generator built.
     *
     * @param S The type of content the registry holds.
     * @param key The key of the registry to look up.
     * @return A lookup into that registry.
     * @since 0.3.0
     */
    fun <S : Any> lookupRegistry(key: ResourceKey<out Registry<S>>) = context.lookup(key)

    /**
     * Looks up a single entry of another data pack registry.
     *
     * @param S The type of content the registry holds.
     * @param key The key of the entry to look up.
     * @return The holder of that entry, or `null` where the registry does not hold it.
     * @since 0.3.0
     */
    fun <S : Any> lookup(key: ResourceKey<S>) = lookupRegistry(key.registryKey()).get(key).getOrNull()

    /**
     * Looks up a tag of another data pack registry.
     *
     * @param S The type of content the registry holds.
     * @param tag The tag to look up.
     * @return The holder set of that tag.
     * @since 0.3.0
     */
    fun <S : Any> lookup(tag: TagKey<S>) = lookupRegistry(tag.registry).get(tag)

    override fun combineFromModules(results: Iterable<Unit>): Unit? {
        if (results.count() <= 0) return null
        return Unit
    }

    override fun combineFromCollections(results: Iterable<Unit>) = combineFromModules(results)

}
