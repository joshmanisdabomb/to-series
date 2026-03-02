package net.jidb.to.base.neoforge.service

import net.jidb.to.base.neoforge.platform.ForgePlatform
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLEnvironment
import net.jidb.to.base.service.EnvironmentService as BaseEnvironmentService

class ForgeEnvironmentService : BaseEnvironmentService() {

    override val platform get() = ForgePlatform
    override val environment get() = if (FMLEnvironment.isProduction()) Environment.BUILD else Environment.DEV
    override val context get() = if (FMLEnvironment.getDist() == Dist.CLIENT) Context.CLIENT else Context.DEDICATED_SERVER

    override fun isModLoaded(modId: String) = ModList.get().isLoaded(modId)

}