package net.jidb.to.base.neoforge.client.service

import net.jidb.to.base.client.service.ClientPlatformService
import net.jidb.to.base.neoforge.client.platform.ScreenForgeClientPlatformModule

class ForgeClientPlatformService : ClientPlatformService() {

    override val screens = ScreenForgeClientPlatformModule

}