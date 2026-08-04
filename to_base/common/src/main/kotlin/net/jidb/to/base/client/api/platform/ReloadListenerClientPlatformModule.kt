package net.jidb.to.base.client.api.platform

import net.jidb.to.base.api.platform.ReloadListenerPlatformModule

/**
 * A [ClientPlatform]-specific module that defines a cross-platform contract for registering reload listeners.
 * The contract is identical to the common one, so nothing is added to [ReloadListenerPlatformModule]. It exists so that a listener registered through it runs against the client's resource packs rather than the server's data packs.
 *
 * @since 0.2.0
 */
abstract class ReloadListenerClientPlatformModule : ReloadListenerPlatformModule()
