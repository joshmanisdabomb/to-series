package net.jidb.to.base.fabric.client.platform

import net.jidb.to.base.client.api.platform.ClientPlatform

/**
 * [ClientPlatform] implementation for Fabric, naming the module that answers each part of the client-side platform on this loader.
 *
 * @since 0.2.0
 */
object FabricClientPlatform : ClientPlatform {

    override val blocks = BlocksFabricClientPlatformModule
    override val entities = EntitiesFabricClientPlatformModule
    override val models = ModelsFabricClientPlatformModule
    override val particles = ParticleFabricClientPlatformModule
    override val screens = ScreenFabricClientPlatformModule
    override val networking = NetworkingFabricClientPlatformModule
    override val reloadListeners = ReloadListenerFabricClientPlatformModule
    override val data = DataFabricClientPlatformModule

}
