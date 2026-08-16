package net.jidb.to.base.neoforge.service

import net.jidb.to.base.neoforge.platform.DeferredForgeEventRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder
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

    override fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>, default: Identifier?, sync: Boolean): Registry<T> {
        val registry = RegistryBuilder(key).apply {
            if (default != null) {
                defaultKey(default)
            }
        }.sync(sync).create()

        Companion.registry.register(key.identifier().namespace) {
            it.register(registry)
        }

        return registry
    }

    companion object {

        val registry = DeferredForgeEventRegistry(NewRegistryEvent::class.java)

        private val registers = mutableMapOf<String, MutableMap<Identifier, DeferredRegister<*>>>()

        fun addListener(modid: String, bus: IEventBus) {
            registers[modid]?.forEach { (key, register) ->
                register.register(bus)
            }
        }

    }

}
