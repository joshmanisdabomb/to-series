package net.jidb.to.base.neoforge.mod

import net.jidb.to.base.api.mod.ToPlatformMod
import net.jidb.to.base.neoforge.platform.NetworkingForgePlatformModule
import net.jidb.to.base.neoforge.platform.ReloadListenerForgePlatformModule
import net.jidb.to.base.neoforge.service.ForgeRegisterService
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

abstract class ToForgeMod : ToPlatformMod {

    open val gameEvents: Any? = null

    open val modEvents: Any? = null

    init {
        common.init()
        init()

        ForgeRegisterService.addListener(common.modid, MOD_BUS)

        MOD_BUS.addListener(::onCommonSetup)
        gameEvents?.also(FORGE_BUS::register)
        modEvents?.also(MOD_BUS::register)

        NetworkingForgePlatformModule.registry.addListener(common.modid, MOD_BUS)
        ReloadListenerForgePlatformModule.registry.addListener(common.modid, FORGE_BUS)
        ForgeRegisterService.registry.addListener(common.modid, MOD_BUS)
    }

    abstract fun subscribeStub(event: FMLConstructModEvent)

    @Suppress("detekt.UnusedParameter")
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        common.setup()
        setup()
    }

}
