package net.jidb.to.base.data.library

import net.jidb.to.base.library.Library
import net.jidb.to.base.library.IResourceKeyLibrary
import net.jidb.to.base.library.SimpleLibrary
import net.minecraft.core.Registry
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import kotlin.jvm.optionals.getOrNull

abstract class DatapackLibrary<T : Any>(modid: String) : SimpleLibrary<T>(modid), IResourceKeyLibrary<T, T, T> {

    lateinit var context: BootstrapContext<T>

    protected fun <S : Any> lookupRegistry(key: ResourceKey<out Registry<S>>) = context.lookup(key)
    protected fun <S : Any> lookup(key: ResourceKey<S>) = lookupRegistry(key.registryKey()).get(key).getOrNull()
    protected fun <S : Any> lookup(tag: TagKey<S>) = lookupRegistry(tag.registry).getOrThrow(tag)

    override fun afterBuild(entry: Library<T, T>.LibraryEntry<out T, out T>) {
        context.register(getEntryResourceKey(entry), entry.value)
    }

}
