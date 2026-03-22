package net.jidb.to.base.platform

abstract class Platform {

    abstract val type: PlatformType
    abstract val blocks: BlocksPlatformModule
    abstract val creativeTabs: CreativeTabsPlatformModule
    abstract val tags: TagsPlatformModule
    abstract val inventory: InventoryPlatformModule

}