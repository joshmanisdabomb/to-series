package net.jidb.to.base.neoforge.client.platform

import net.jidb.to.base.client.api.platform.ReloadListenerClientPlatformModule
import net.jidb.to.base.neoforge.platform.DeferredForgeEventRegistry
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent

object ReloadListenerForgeClientPlatformModule : ReloadListenerClientPlatformModule() {

    val registry = DeferredForgeEventRegistry(AddClientReloadListenersEvent::class.java)

    override fun register(identifier: Identifier, listener: PreparableReloadListener) {
        registry.register(identifier.namespace) { event -> event.addListener(identifier, listener) }
    }

}
