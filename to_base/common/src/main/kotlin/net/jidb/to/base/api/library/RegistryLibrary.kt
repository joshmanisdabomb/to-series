package net.jidb.to.base.api.library

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

sealed class RegistryLibrary<I, V : Any>(modid: String) : Library<I, V>(modid), IResourceKeyLibrary<I, V, V> {

    abstract val registry: Registry<V>
    override val registryKey: ResourceKey<out Registry<V>> get() = registry.key()

}