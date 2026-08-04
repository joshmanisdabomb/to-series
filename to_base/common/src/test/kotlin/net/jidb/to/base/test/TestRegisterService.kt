package net.jidb.to.base.test

import com.mojang.serialization.Lifecycle
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.jidb.to.base.service.RegisterService as BaseRegisterService

/**
 * [net.jidb.to.base.service.RegisterService] implementation for the unit tests, loaded through Java's service loader from `src/test/resources/META-INF/services`.
 *
 * There is no modloader under a plain JUnit run, so this writes straight into the registry the way Fabric's implementation does, and builds a plain [MappedRegistry] where Fabric and Neoforge each go through a builder of their own.
 * Registering into a built-in registry only works while it is still unfrozen, i.e. before [MinecraftBootstrap] has run.
 *
 * @since 0.8.0
 */
class TestRegisterService : BaseRegisterService() {

    override fun <T : Any, E : T> register(registry: Registry<T>, key: Identifier, value: (key: Identifier) -> E): () -> E {
        val holder = Registry.registerForHolder(registry, key, value(key))
        return holder::value
    }

    override fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>, default: Identifier?, sync: Boolean): Registry<T> = MappedRegistry(key, Lifecycle.stable())

}
