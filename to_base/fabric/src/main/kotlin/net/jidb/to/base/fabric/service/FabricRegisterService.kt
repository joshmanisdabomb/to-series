package net.jidb.to.base.fabric.service

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.jidb.to.base.service.RegisterService as BaseRegisterService

class FabricRegisterService : BaseRegisterService() {
    override fun <T : Any, E : T> register(registry: Registry<T>, key: Identifier, value: (key: Identifier) -> E): () -> E {
        val holder = Registry.registerForHolder(registry, key, value(key))
        return holder::value
    }

    override fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>, default: Identifier?, sync: Boolean): Registry<T> {
        return when (default) {
            null -> FabricRegistryBuilder.createSimple(key)
            else -> FabricRegistryBuilder.createDefaulted(key, default)
        }.apply {
            if (sync) {
                attribute(RegistryAttribute.SYNCED)
            }
        }.buildAndRegister()
    }
}