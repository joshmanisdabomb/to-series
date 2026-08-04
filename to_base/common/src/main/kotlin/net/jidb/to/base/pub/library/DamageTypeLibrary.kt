package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.ResourceKeyLibrary
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType

/**
 * [ResourceKeyLibrary] implementation that declares a [ResourceKey] for each [DamageType] the mod adds, and provides access to those keys in one place.
 * A damage type is data pack content rather than registry content, so the library holds only the keys; [getSource] turns one into the [DamageSource] that damage is actually dealt with.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.8.0
 */
open class DamageTypeLibrary(modid: String) : ResourceKeyLibrary<DamageType>(modid) {

    override val registryKey = Registries.DAMAGE_TYPE

    /**
     * The [DamageSource] built for each declared damage type, cached per [RegistryAccess] because a source holds a reference into the registries of the level it came from.
     *
     * @since 0.8.0
     */
    private val sources = mutableMapOf<RegistryAccess, MutableMap<ResourceKey<DamageType>, DamageSource>>()

    /**
     * Retrieves the [DamageSource] for a declared damage type, building and caching it on first use.
     *
     * @param type The key of the damage type to get a source for.
     * @param registries The registries of the level the damage is being dealt in.
     * @return The [DamageSource] for the given damage type.
     * @throws IllegalStateException If the damage type is not present in the given registries, i.e. its data pack entry is missing.
     * @since 0.8.0
     */
    fun getSource(type: ResourceKey<DamageType>, registries: RegistryAccess) = sources.getOrPut(registries) { mutableMapOf() }.getOrPut(type) {
        DamageSource(registries.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(type))
    }

}
