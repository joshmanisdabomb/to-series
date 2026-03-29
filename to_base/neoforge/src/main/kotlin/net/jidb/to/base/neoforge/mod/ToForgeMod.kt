package net.jidb.to.base.neoforge.mod

import net.jidb.to.base.mod.ToPlatformMod
import net.jidb.to.base.neoforge.platform.NetworkingForgePlatformModule
import net.jidb.to.base.neoforge.platform.ReloadListenerForgePlatformModule
import net.jidb.to.base.neoforge.service.ForgeRegisterService
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

abstract class ToForgeMod : ToPlatformMod {
    init {
        common.init()
        init()

        ForgeRegisterService.addListener(common.modid, MOD_BUS)

        MOD_BUS.addListener(::onCommonSetup)

        NetworkingForgePlatformModule.registry.addListener(common.modid, MOD_BUS)
        ReloadListenerForgePlatformModule.registry.addListener(common.modid, FORGE_BUS)
    }

    fun onCommonSetup(event: FMLCommonSetupEvent) {
        common.setup()
        setup()
    }
}