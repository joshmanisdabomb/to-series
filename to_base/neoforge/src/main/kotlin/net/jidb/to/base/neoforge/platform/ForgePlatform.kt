package net.jidb.to.base.neoforge.platform

import net.jidb.to.base.api.platform.Platform
import net.jidb.to.base.api.platform.PlatformType

/**
 * [Platform] implementation for Neoforge, naming the module that answers each part of the platform on this loader.
 *
 * @since 0.0.3
 */
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
