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

/**
 * [net.jidb.to.base.service.RegisterService] implementation for Neoforge, loaded through Java's service loader from the entry this loader project registers.
 *
 * Nothing is registered as it is declared, because Neoforge registers through a deferred register bound to a mod's bus; a register is opened per mod and per registry as declarations arrive, and they are all bound at once when the mod comes up.
 *
 * @since 0.0.3
 */
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

        /**
         * The queued registry creations, played back when Neoforge raises its new registry event.
         *
         * @since 0.5.0
         */
        val registry = DeferredForgeEventRegistry(NewRegistryEvent::class.java)

        /**
         * The deferred register everything is declared into, keyed by mod ID and then by the registry it belongs to.
         *
         * @since 0.0.3
         */
        private val registers = mutableMapOf<String, MutableMap<Identifier, DeferredRegister<*>>>()

        /**
         * Binds every register a mod opened to its bus, which is what actually registers what was declared.
         *
         * @param modid The mod ID whose registers are being bound.
         * @param bus The mod's bus.
         * @since 0.0.3
         */
        fun addListener(modid: String, bus: IEventBus) {
            registers[modid]?.forEach { (key, register) ->
                register.register(bus)
            }
        }

    }

}
