package net.jidb.to.base.client.api.platform

interface ClientPlatform {

    val blocks: BlocksClientPlatformModule
    val entities: EntitiesClientPlatformModule
    val models: ModelsClientPlatformModule
    val particles: ParticleClientPlatformModule
    val screens: ScreenClientPlatformModule
    val networking: NetworkingClientPlatformModule
    val reloadListeners: ReloadListenerClientPlatformModule
    val data: DataClientPlatformModule

}