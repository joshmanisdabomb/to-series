package net.jidb.to.base.fabric.client.service

import net.jidb.to.base.client.service.ClientPlatformService
import net.jidb.to.base.fabric.client.platform.FabricClientPlatform

/**
 * [ClientPlatformService] implementation for Fabric, loaded through Java's service loader from the entry this loader project registers.
 *
 * @since 0.1.0
 */
class FabricClientPlatformService : ClientPlatformService() {

    override val platform = FabricClientPlatform

}
