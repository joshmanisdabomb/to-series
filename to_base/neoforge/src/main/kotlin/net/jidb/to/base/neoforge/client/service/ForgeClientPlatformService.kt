package net.jidb.to.base.neoforge.client.service

import net.jidb.to.base.client.service.ClientPlatformService
import net.jidb.to.base.neoforge.client.platform.ForgeClientPlatform

class ForgeClientPlatformService : ClientPlatformService() {

    override val platform = ForgeClientPlatform

}
