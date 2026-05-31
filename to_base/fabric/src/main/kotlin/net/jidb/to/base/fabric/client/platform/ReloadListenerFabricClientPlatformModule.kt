package net.jidb.to.base.fabric.client.platform

import net.fabricmc.fabric.api.resource.v1.ResourceLoader
import net.jidb.to.base.client.platform.ReloadListenerClientPlatformModule
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener

object ReloadListenerFabricClientPlatformModule : ReloadListenerClientPlatformModule() {

    override fun register(identifier: Identifier, listener: PreparableReloadListener) = ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(identifier, listener)

}
