package net.jidb.to.base.neoforge.client.mod

import net.jidb.to.base.client.api.mod.ToPlatformClientMod
import net.jidb.to.base.neoforge.client.data.mod.ToForgeDataMod
import net.jidb.to.base.neoforge.client.platform.*
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.neoforge.data.event.GatherDataEvent
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

abstract class ToForgeClientMod : ToPlatformClientMod {
    open val eventHandler: Any? = null
    open val data: ((event: GatherDataEvent.Client) -> ToForgeDataMod)? = null

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
        ModelsForgeClientPlatformModule.layer_registry.addListener(client.common.modid, MOD_BUS)
        ModelsForgeClientPlatformModule.special_registry.addListener(client.common.modid, MOD_BUS)
    }

    abstract fun subscribeStub(event: FMLConstructModEvent)

    fun onClientSetup(event: FMLClientSetupEvent) {
        client.clientSetup()
        clientSetup()
    }

    fun onGatherData(event: GatherDataEvent.Client) {
        val getter = data ?: error("Data mod is not set.")
        val data = getter(event)
        data.generate()
    }
}