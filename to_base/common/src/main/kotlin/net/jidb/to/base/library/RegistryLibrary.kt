package net.jidb.to.base.library

import net.minecraft.core.Registry

sealed class RegistryLibrary<I, V : Any>(modid: String) : Library<I, V>(modid), RegistryKeyLibrary<I, V, V> {

    abstract override val registry: Registry<V>

}