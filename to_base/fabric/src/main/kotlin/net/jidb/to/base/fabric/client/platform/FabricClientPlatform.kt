package net.jidb.to.base.fabric.client.platform

import net.jidb.to.base.client.platform.ClientPlatform

object FabricClientPlatform : ClientPlatform {

    override val blocks = BlocksFabricClientPlatformModule
    override val entities = EntitiesFabricClientPlatformModule
    override val particles = ParticleFabricClientPlatformModule
    override val screens = ScreenFabricClientPlatformModule
    override val networking = NetworkingFabricClientPlatformModule
    override val reloadListeners = ReloadListenerFabricClientPlatformModule
    override val data = DataFabricClientPlatformModule

}
