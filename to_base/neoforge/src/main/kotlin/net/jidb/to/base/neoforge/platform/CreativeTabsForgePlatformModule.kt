package net.jidb.to.base.neoforge.platform

import net.jidb.to.base.platform.CreativeTabsPlatformModule
import net.minecraft.world.item.CreativeModeTab

object CreativeTabsForgePlatformModule : CreativeTabsPlatformModule() {

    override fun builder() = CreativeModeTab.builder()

}
