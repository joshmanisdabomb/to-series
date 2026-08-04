package net.jidb.to.base.service

import java.util.ServiceLoader

/**
 * Entry point for the services that the current modloader provides an implementation of.
 * Each service is an abstract class in common code, loaded here through Java's [ServiceLoader] from the implementation the loader project registers.
 *
 * Usually accessed statically for the service that is needed:
 * ```kotlin
 * Services.platform
 * ```
 *
 * See [net.jidb.to.base.client.service.ClientServices] for the services that only exist on the client side.
 *
 * @since 0.0.3
 */
object Services {

    /**
     * The loaded [EnvironmentService], which answers questions about the modloader and physical side the game is currently running under.
     *
     * @since 0.0.3
     */
    val environment = this(EnvironmentService::class.java)

    /**
     * The loaded [RegisterService], which registers game objects into a [net.minecraft.core.Registry] the way the current modloader expects.
     *
     * @since 0.0.3
     */
    val register = this(RegisterService::class.java)

    /**
     * The current [net.jidb.to.base.api.platform.Platform], a shortcut for [EnvironmentService.platform].
     *
     * @since 0.2.0
     */
    val platform get() = environment.platform

    /**
     * The current [net.jidb.to.base.api.side.Side], a shortcut for [EnvironmentService.side].
     *
     * @since 0.6.0
     */
    val side get() = environment.side

    /**
     * Loads the single implementation of the given service class, so that the object itself can be called like a function.
     *
     * @param T The type of the service to load.
     * @param clazz The class of the service to load.
     * @return The implementation of the service registered by the current modloader.
     * @throws IllegalStateException If no implementation of the service is registered.
     * @since 0.0.3
     */
    operator fun <T> invoke(clazz: Class<T>) = load(clazz)

    /**
     * Loads the single implementation of the given service class through Java's [ServiceLoader].
     * Each loader project declares its implementations in `META-INF/services`, so exactly one is expected to be found.
     *
     * @param T The type of the service to load.
     * @param clazz The class of the service to load.
     * @return The implementation of the service registered by the current modloader.
     * @throws IllegalStateException If no implementation of the service is registered.
     * @since 0.1.0
     */
    fun <T> load(clazz: Class<T>) = ServiceLoader.load(clazz).findFirst()
        .orElseThrow { IllegalStateException("Failed to load service for ${clazz.name}.") }

}
