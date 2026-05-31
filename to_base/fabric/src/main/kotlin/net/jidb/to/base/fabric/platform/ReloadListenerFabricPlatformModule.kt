package net.jidb.to.base.fabric.platform

import net.fabricmc.fabric.api.resource.v1.ResourceLoader
import net.jidb.to.base.platform.ReloadListenerPlatformModule
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener

object ReloadListenerFabricPlatformModule : ReloadListenerPlatformModule() {

    override fun register(identifier: Identifier, listener: PreparableReloadListener) = ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(identifier, listener)

}
