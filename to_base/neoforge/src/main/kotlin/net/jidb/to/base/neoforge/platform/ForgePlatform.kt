package net.jidb.to.base.neoforge.platform

import net.jidb.to.base.api.platform.Platform
import net.jidb.to.base.api.platform.PlatformType

object ForgePlatform : Platform {

    override val type = PlatformType.NEOFORGE
    override val blocks = BlocksForgePlatformModule
    override val creativeTabs = CreativeTabsForgePlatformModule
    override val tags = TagsForgePlatformModule
    override val inventory = InventoryForgePlatformModule
    override val networking = NetworkingForgePlatformModule
    override val biomes = BiomeForgePlatformModule
    override val reloadListeners = ReloadListenerForgePlatformModule
    override val transfer = TransferForgePlatformModule

}
