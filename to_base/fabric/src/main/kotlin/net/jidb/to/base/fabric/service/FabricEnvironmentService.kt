package net.jidb.to.base.fabric.service

import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.jidb.to.base.api.side.ServerSide
import net.jidb.to.base.client.api.side.ClientSide
import net.jidb.to.base.fabric.platform.FabricPlatform
import kotlin.jvm.optionals.getOrNull
import net.jidb.to.base.service.EnvironmentService as BaseEnvironmentService

/**
 * [net.jidb.to.base.service.EnvironmentService] implementation for Fabric, loaded through Java's service loader from the entry this loader project registers.
 *
 * @since 0.0.3
 */
class FabricEnvironmentService : BaseEnvironmentService() {

    override val platform get() = FabricPlatform
    override val environment get() = if (FabricLoader.getInstance().isDevelopmentEnvironment) Environment.DEV else Environment.BUILD
    override val side = if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) ClientSide() else ServerSide()

    override fun isModLoaded(modId: String) = FabricLoader.getInstance().isModLoaded(modId)
    override fun getModVersion(modId: String) = FabricLoader.getInstance().getModContainer(modId).getOrNull()?.metadata?.version?.friendlyString

}
