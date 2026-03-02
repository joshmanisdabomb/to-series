package net.jidb.to.base.fabric.platform

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.jidb.to.base.platform.CreativeTabsPlatformModule

object CreativeTabsFabricPlatformModule : CreativeTabsPlatformModule() {

    override fun builder() = FabricItemGroup.builder()

}
