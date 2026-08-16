package net.jidb.to.base.neoforge.client.mod

import net.jidb.to.base.client.api.mod.ToPlatformClientMod
import net.jidb.to.base.neoforge.client.platform.BlocksForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.platform.EntitiesForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.platform.ModelsForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.platform.NetworkingForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.platform.ParticleForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.platform.ReloadListenerForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.platform.ScreenForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.service.ForgeDataModService
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.neoforge.data.event.GatherDataEvent
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.util.ServiceLoader

abstract class ToForgeClientMod : ToPlatformClientMod {

    open val eventHandler: Any? = null

    init {
        client.clientInit()
        clientInit()

        MOD_BUS.addListener(::onClientSetup)
        MOD_BUS.addListener(::onGatherData)
        eventHandler?.also(FORGE_BUS::register)

        BlocksForgeClientPlatformModule.registry.addListener(client.common.modid, MOD_BUS)
        EntitiesForgeClientPlatformModule.registry.addListener(client.common.modid, MOD_BUS)
        NetworkingForgeClientPlatformModule.registry.addListener(client.common.modid, MOD_BUS)
        ParticleForgeClientPlatformModule.registry.addListener(client.common.modid, MOD_BUS)
        ScreenForgeClientPlatformModule.registry.addListener(client.common.modid, MOD_BUS)
        ReloadListenerForgeClientPlatformModule.registry.addListener(client.common.modid, MOD_BUS)
        ModelsForgeClientPlatformModule.layerRegistry.addListener(client.common.modid, MOD_BUS)
        ModelsForgeClientPlatformModule.specialRegistry.addListener(client.common.modid, MOD_BUS)
        ModelsForgeClientPlatformModule.blockTintRegistry.addListener(client.common.modid, MOD_BUS)
        ModelsForgeClientPlatformModule.itemTintRegistry.addListener(client.common.modid, MOD_BUS)
    }

    abstract fun subscribeStub(event: FMLConstructModEvent)

    @Suppress("detekt.UnusedParameter")
    fun onClientSetup(event: FMLClientSetupEvent) {
        client.clientSetup()
        clientSetup()
    }

    fun onGatherData(event: GatherDataEvent.Client) {
        val modid = client.common.modid
        val service = ServiceLoader.load(ForgeDataModService::class.java).firstOrNull { it.modid == modid }
        (service ?: error("Data mod service is not registered for $modid.")).generate(event)
    }

}
