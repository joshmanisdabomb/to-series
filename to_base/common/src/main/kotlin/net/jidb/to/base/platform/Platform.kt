package net.jidb.to.base.platform

interface Platform {

    val type: PlatformType

    val blocks: BlocksPlatformModule
    val creativeTabs: CreativeTabsPlatformModule
    val tags: TagsPlatformModule
    val inventory: InventoryPlatformModule
    val networking: NetworkingPlatformModule
    val reloadListeners: ReloadListenerPlatformModule

}