package net.jidb.to.base.fabric.platform

import net.jidb.to.base.api.platform.Platform
import net.jidb.to.base.api.platform.PlatformType

object FabricPlatform : Platform {

    override val type = PlatformType.FABRIC
    override val blocks = BlocksFabricPlatformModule
    override val creativeTabs = CreativeTabsFabricPlatformModule
    override val tags = TagsFabricPlatformModule
    override val inventory = InventoryFabricPlatformModule
    override val networking = NetworkingFabricPlatformModule
    override val biomes = BiomeFabricPlatformModule
    override val reloadListeners = ReloadListenerFabricPlatformModule
    override val transfer = TransferFabricPlatformModule

}