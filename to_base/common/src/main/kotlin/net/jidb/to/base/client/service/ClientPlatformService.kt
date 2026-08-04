package net.jidb.to.base.client.service

import net.jidb.to.base.client.api.platform.ClientPlatform

/**
 * A service that supplies the [ClientPlatform] for the modloader currently running, the client-side counterpart of [net.jidb.to.base.service.EnvironmentService.platform].
 * It is kept separate from the common services so that nothing on the server side can reach a client-only class through them.
 * Each loader project provides its own implementation, loaded through [ClientServices].
 *
 * Usually accessed from the [ClientServices] object:
 * ```kotlin
 * ClientServices.platform
 * ```
 *
 * @since 0.1.0
 */
abstract class ClientPlatformService {

    /**
     * The [ClientPlatform] for the modloader currently running, which exposes the cross-platform modules for client-side loader-specific code.
     *
     * @since 0.1.0
     */
    abstract val platform: ClientPlatform

}
