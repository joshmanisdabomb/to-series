package net.jidb.to.base.neoforge.service

import net.jidb.to.base.api.side.ServerSide
import net.jidb.to.base.client.api.side.ClientSide
import net.jidb.to.base.neoforge.platform.ForgePlatform
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLEnvironment
import kotlin.jvm.optionals.getOrNull
import net.jidb.to.base.service.EnvironmentService as BaseEnvironmentService

class ForgeEnvironmentService : BaseEnvironmentService() {

    override val platform get() = ForgePlatform
    override val environment get() = if (FMLEnvironment.isProduction()) Environment.BUILD else Environment.DEV
    override val side = if (FMLEnvironment.getDist() == Dist.CLIENT) ClientSide() else ServerSide()

    override fun isModLoaded(modId: String) = ModList.get().isLoaded(modId)
    override fun getModVersion(modId: String) = ModList.get().getModContainerById(modId).getOrNull()?.modInfo?.version?.toString()

}
