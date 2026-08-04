package net.jidb.to.base.fabric.client.platform

import net.fabricmc.fabric.api.resource.v1.ResourceLoader
import net.jidb.to.base.client.api.platform.ReloadListenerClientPlatformModule
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener

/**
 * [ReloadListenerClientPlatformModule] implementation for Fabric, registering against the client's own resources rather than the server's data.
 *
 * @since 0.2.0
 */
object ReloadListenerFabricClientPlatformModule : ReloadListenerClientPlatformModule() {

    override fun register(identifier: Identifier, listener: PreparableReloadListener) = ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(identifier, listener)

}
