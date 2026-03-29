package net.jidb.to.base.fabric.client.service

import net.jidb.to.base.client.service.ClientPlatformService
import net.jidb.to.base.fabric.client.platform.FabricClientPlatform

class FabricClientPlatformService : ClientPlatformService() {

    override val platform = FabricClientPlatform

}