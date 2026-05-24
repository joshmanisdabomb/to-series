package net.jidb.to.base.neoforge.client.platform

import net.jidb.to.base.client.platform.ClientPlatform

object ForgeClientPlatform : ClientPlatform {

    override val blocks = BlocksForgeClientPlatformModule
    override val entities = EntitiesForgeClientPlatformModule
    override val particles = ParticleForgeClientPlatformModule
    override val screens = ScreenForgeClientPlatformModule
    override val networking = NetworkingForgeClientPlatformModule
    override val reloadListeners = ReloadListenerForgeClientPlatformModule
    override val data = DataForgeClientPlatformModule

}
