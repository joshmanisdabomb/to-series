package net.jidb.to.base.neoforge.platform

import net.jidb.to.base.api.platform.ReloadListenerPlatformModule
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.neoforged.neoforge.event.AddServerReloadListenersEvent

object ReloadListenerForgePlatformModule : ReloadListenerPlatformModule() {

    val registry = DeferredForgeEventRegistry(AddServerReloadListenersEvent::class.java)

    override fun register(identifier: Identifier, listener: PreparableReloadListener) {
        registry.register(identifier.namespace) { event -> event.addListener(identifier, listener) }
    }

}
