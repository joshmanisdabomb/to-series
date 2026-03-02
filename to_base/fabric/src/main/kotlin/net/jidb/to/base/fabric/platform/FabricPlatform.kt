package net.jidb.to.base.fabric.platform

import net.jidb.to.base.platform.CreativeTabsPlatformModule
import net.jidb.to.base.platform.Platform
import net.jidb.to.base.platform.PlatformType

object FabricPlatform : Platform() {

    override val type = PlatformType.FABRIC
    override val creativeTabs = CreativeTabsFabricPlatformModule

}