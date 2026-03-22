package net.jidb.to.base.fabric.client.service

import net.jidb.to.base.client.service.ClientPlatformService
import net.jidb.to.base.fabric.client.platform.ScreenFabricClientPlatformModule

class FabricClientPlatformService : ClientPlatformService() {

    override val screens = ScreenFabricClientPlatformModule

}