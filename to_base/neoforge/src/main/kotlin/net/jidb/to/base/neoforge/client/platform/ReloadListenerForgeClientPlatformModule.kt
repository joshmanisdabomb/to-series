package net.jidb.to.base.neoforge.client.platform

import net.jidb.to.base.client.api.platform.ReloadListenerClientPlatformModule
import net.jidb.to.base.neoforge.platform.DeferredForgeEventRegistry
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent

/**
 * [ReloadListenerClientPlatformModule] implementation for Neoforge, registering against the client's own resources rather than the server's data.
 *
 * A listener cannot be registered whenever a mod asks, only while Neoforge is raising its own event, so every declaration is queued in a [DeferredForgeEventRegistry] and played back once that arrives.
 *
 * @since 0.2.0
 */
object ReloadListenerForgeClientPlatformModule : ReloadListenerClientPlatformModule() {

    /**
     * The queued registrations, played back when Neoforge raises the event they are waiting on.
     *
     * @since 0.2.0
     */
    val registry = DeferredForgeEventRegistry(AddClientReloadListenersEvent::class.java)

    override fun register(identifier: Identifier, listener: PreparableReloadListener) {
        registry.register(identifier.namespace) { event -> event.addListener(identifier, listener) }
    }

}
