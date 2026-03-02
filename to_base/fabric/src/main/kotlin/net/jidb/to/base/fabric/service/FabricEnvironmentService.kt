package net.jidb.to.base.fabric.service

import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.jidb.to.base.fabric.platform.FabricPlatform
import net.jidb.to.base.service.EnvironmentService as BaseEnvironmentService

class FabricEnvironmentService : BaseEnvironmentService() {

    override val platform get() = FabricPlatform
    override val environment get() = if (FabricLoader.getInstance().isDevelopmentEnvironment) Environment.DEV else Environment.BUILD
    override val context get() = if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) Context.CLIENT else Context.DEDICATED_SERVER

    override fun isModLoaded(modId: String) = FabricLoader.getInstance().isModLoaded(modId)

}