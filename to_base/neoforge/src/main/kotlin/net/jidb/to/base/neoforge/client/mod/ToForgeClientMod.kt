package net.jidb.to.base.neoforge.client.mod

import net.jidb.to.base.client.mod.ToPlatformClientMod
import net.jidb.to.base.neoforge.client.platform.NetworkingForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.platform.ParticleForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.platform.ReloadListenerForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.platform.ScreenForgeClientPlatformModule
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

abstract class ToForgeClientMod : ToPlatformClientMod {
    init {
        client.clientInit()
        clientInit()

        MOD_BUS.addListener(::onClientSetup)

        NetworkingForgeClientPlatformModule.registry.addListener(client.common.modid, MOD_BUS)
        ParticleForgeClientPlatformModule.registry.addListener(client.common.modid, MOD_BUS)
        ScreenForgeClientPlatformModule.registry.addListener(client.common.modid, MOD_BUS)
        ReloadListenerForgeClientPlatformModule.registry.addListener(client.common.modid, MOD_BUS)
    }

    abstract fun subscribeStub(event: FMLConstructModEvent)

    fun onClientSetup(event: FMLClientSetupEvent) {
        client.clientSetup()
        clientSetup()
    }
}