package net.jidb.to.base.neoforge.client.service

import net.jidb.to.base.client.service.ClientPlatformService
import net.jidb.to.base.neoforge.client.platform.ForgeClientPlatform

/**
 * [ClientPlatformService] implementation for Neoforge, loaded through Java's service loader from the entry this loader project registers.
 *
 * @since 0.1.0
 */
class ForgeClientPlatformService : ClientPlatformService() {

    override val platform = ForgeClientPlatform

}
