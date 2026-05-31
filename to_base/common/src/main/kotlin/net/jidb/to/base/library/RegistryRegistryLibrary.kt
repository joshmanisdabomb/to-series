package net.jidb.to.base.library

import net.jidb.to.base.service.Services
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

abstract class RegistryRegistryLibrary(modid: String) : SimpleLibrary<Registry<*>>(modid) {

    operator fun <T : Any> invoke(key: ResourceKey<Registry<T>>? = null, default: Identifier? = null, sync: Boolean = false): Library<Registry<*>, Registry<*>>.LibraryEntry<Registry<T>, Registry<T>> {
        return this.LibraryEntry({
            val registry = it()
            return@LibraryEntry { registry }
        }, { Services.register.createRegistry(key ?: ResourceKey.createRegistryKey(it.id), default, sync) })
    }

}