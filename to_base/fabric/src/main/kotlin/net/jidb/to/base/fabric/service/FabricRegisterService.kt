package net.jidb.to.base.fabric.service

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.jidb.to.base.service.RegisterService as BaseRegisterService

class FabricRegisterService : BaseRegisterService() {
    override fun <T : Any, E : T> register(registry: Registry<T>, key: Identifier, value: (key: Identifier) -> E): () -> E {
        val holder = Registry.registerForHolder(registry, key, value(key))
        return holder::value
    }
}