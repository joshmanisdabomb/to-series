package net.jidb.to.base.neoforge.platform

import net.jidb.to.base.platform.Platform
import net.jidb.to.base.platform.PlatformType

object ForgePlatform : Platform() {

    override val type = PlatformType.NEOFORGE
    override val creativeTabs = CreativeTabsForgePlatformModule

}