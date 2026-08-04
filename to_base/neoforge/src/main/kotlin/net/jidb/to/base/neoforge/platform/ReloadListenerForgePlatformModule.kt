package net.jidb.to.base.neoforge.platform

import net.jidb.to.base.api.platform.ReloadListenerPlatformModule
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.neoforged.neoforge.event.AddServerReloadListenersEvent

/**
 * [ReloadListenerPlatformModule] implementation for Neoforge, registering against the server's data rather than the client's resources.
 *
 * A listener cannot be registered whenever a mod asks, only while Neoforge is raising its own event, so every declaration is queued in a [DeferredForgeEventRegistry] and played back once that arrives.
 *
 * @since 0.2.0
 */
object ReloadListenerForgePlatformModule : ReloadListenerPlatformModule() {

    /**
     * The queued listener registrations, played back when Neoforge raises its reload listener event.
     *
     * @since 0.2.0
     */
    val registry = DeferredForgeEventRegistry(AddServerReloadListenersEvent::class.java)

    override fun register(identifier: Identifier, listener: PreparableReloadListener) {
        registry.register(identifier.namespace) { event -> event.addListener(identifier, listener) }
    }

}
