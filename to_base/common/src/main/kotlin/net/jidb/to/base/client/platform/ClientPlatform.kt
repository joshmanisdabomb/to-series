package net.jidb.to.base.client.platform

interface ClientPlatform {

    val rendering: RenderClientPlatformModule
    val particles: ParticleClientPlatformModule
    val screens: ScreenClientPlatformModule
    val networking: NetworkingClientPlatformModule
    val reloadListeners: ReloadListenerClientPlatformModule

}