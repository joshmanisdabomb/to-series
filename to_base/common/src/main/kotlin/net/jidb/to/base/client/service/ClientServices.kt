package net.jidb.to.base.client.service

import net.jidb.to.base.service.Services

/**
 * Entry point for the services that only exist on the client side, the client-side counterpart of [Services].
 * Loading is delegated to [Services.load], so the two objects share one [java.util.ServiceLoader] mechanism and differ only in which classes they are allowed to ask for.
 *
 * Usually accessed statically for the service that is needed:
 * ```kotlin
 * ClientServices.platform
 * ```
 *
 * @since 0.1.0
 */
object ClientServices {

    /**
     * The loaded [ClientPlatformService], kept private so that callers reach the [ClientPlatform][net.jidb.to.base.client.api.platform.ClientPlatform] through [platform] rather than the service wrapping it.
     *
     * @since 0.1.0
     */
    private val _platform = this(ClientPlatformService::class.java)

    /**
     * The current [net.jidb.to.base.client.api.platform.ClientPlatform], a shortcut for [ClientPlatformService.platform].
     *
     * @since 0.1.0
     */
    val platform get() = _platform.platform

    /**
     * Loads the single implementation of the given client service class, so that the object itself can be called like a function.
     *
     * @param T The type of the service to load.
     * @param clazz The class of the service to load.
     * @return The implementation of the service registered by the current modloader.
     * @throws IllegalStateException If no implementation of the service is registered.
     * @since 0.1.0
     */
    operator fun <T> invoke(clazz: Class<T>) = Services.load(clazz)

}
