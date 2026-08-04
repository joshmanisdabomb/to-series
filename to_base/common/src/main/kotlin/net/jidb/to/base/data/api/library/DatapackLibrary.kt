package net.jidb.to.base.data.api.library

import net.jidb.to.base.api.library.IResourceKeyLibrary
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.minecraft.core.Registry
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import kotlin.jvm.optionals.getOrNull

/**
 * [SimpleLibrary] implementation that registers data pack content as the library is built, and provides access to it in one place.
 * Unlike a [net.jidb.to.base.api.library.RegistryLibrary], which registers into a live registry, this writes into the bootstrap context of a generator run, so [context] has to be set before the library is built.
 *
 * @param T The type of data pack content this library holds.
 * @param modid The mod ID associated with the library.
 * @since 0.3.0
 */
abstract class DatapackLibrary<T : Any>(modid: String) : SimpleLibrary<T>(modid), IResourceKeyLibrary<T, T, T> {

    /**
     * The bootstrap context entries are registered into and looked up through, set by the generator before the library is built.
     *
     * @since 0.3.0
     */
    lateinit var context: BootstrapContext<T>

    /**
     * Looks up another data pack registry, which is how a declared entry refers to one that another generator built.
     *
     * @param S The type of content the registry holds.
     * @param key The key of the registry to look up.
     * @return A lookup into that registry.
     * @since 0.3.0
     */
    protected fun <S : Any> lookupRegistry(key: ResourceKey<out Registry<S>>) = context.lookup(key)

    /**
     * Looks up a single entry of another data pack registry.
     *
     * @param S The type of content the registry holds.
     * @param key The key of the entry to look up.
     * @return The holder of that entry, or `null` where the registry does not hold it.
     * @since 0.3.0
     */
    protected fun <S : Any> lookup(key: ResourceKey<S>) = lookupRegistry(key.registryKey()).get(key).getOrNull()

    /**
     * Looks up a tag of another data pack registry.
     *
     * @param S The type of content the registry holds.
     * @param tag The tag to look up.
     * @return The holder set of that tag.
     * @throws IllegalStateException If the registry does not hold the tag.
     * @since 0.3.0
     */
    protected fun <S : Any> lookup(tag: TagKey<S>) = lookupRegistry(tag.registry).getOrThrow(tag)

    override fun afterBuild(entry: Library<T, T>.LibraryEntry<out T, out T>) {
        context.register(getEntryResourceKey(entry), entry.value)
    }

}
