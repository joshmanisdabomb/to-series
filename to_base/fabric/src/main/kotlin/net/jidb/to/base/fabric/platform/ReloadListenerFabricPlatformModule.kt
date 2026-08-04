package net.jidb.to.base.fabric.platform

import net.fabricmc.fabric.api.resource.v1.ResourceLoader
import net.jidb.to.base.api.platform.ReloadListenerPlatformModule
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener

/**
 * [ReloadListenerPlatformModule] implementation for Fabric, registering against the server's data rather than the client's resources.
 *
 * @since 0.2.0
 */
object ReloadListenerFabricPlatformModule : ReloadListenerPlatformModule() {

    override fun register(identifier: Identifier, listener: PreparableReloadListener) = ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(identifier, listener)

}
