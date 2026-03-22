package net.jidb.to.base.neoforge.service

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.DeferredRegister
import net.jidb.to.base.service.RegisterService as BaseRegisterService

@EventBusSubscriber
class ForgeRegisterService : BaseRegisterService() {

    override fun <T : Any, E : T> register(registry: Registry<T>, key: Identifier, value: (key: Identifier) -> E): () -> E {
        val register = registers.getOrPut(key.namespace) { mutableMapOf() }.getOrPut(registry.key().identifier()) {
            DeferredRegister.create(registry, key.namespace)
        } as DeferredRegister<T>
        val holder = register.register(key.path, value)

        return holder::value
    }

    companion object {
        private val registers = mutableMapOf<String, MutableMap<Identifier, DeferredRegister<*>>>()

        fun registerMod(modid: String, bus: IEventBus) {
            registers[modid]?.forEach { (key, register) ->
                register.register(bus)
            }
        }
    }

}