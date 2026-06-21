package net.jidb.to.base.api.platform

interface Platform {

    val type: PlatformType

    val blocks: BlocksPlatformModule
    val creativeTabs: CreativeTabsPlatformModule
    val tags: TagsPlatformModule
    val inventory: InventoryPlatformModule
    val networking: NetworkingPlatformModule
    val biomes: BiomePlatformModule
    val reloadListeners: ReloadListenerPlatformModule
    val transfer: TransferPlatformModule

}