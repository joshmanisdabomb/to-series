package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.ResourceKeyLibrary
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType

open class DamageTypeLibrary(modid: String) : ResourceKeyLibrary<DamageType>(modid) {

    override val registryKey = Registries.DAMAGE_TYPE

    private val sources = mutableMapOf<RegistryAccess, MutableMap<ResourceKey<DamageType>, DamageSource>>()

    fun getSource(type: ResourceKey<DamageType>, registries: RegistryAccess) = sources.getOrPut(registries) { mutableMapOf() }.getOrPut(type) {
        DamageSource(registries.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(type))
    }

}
