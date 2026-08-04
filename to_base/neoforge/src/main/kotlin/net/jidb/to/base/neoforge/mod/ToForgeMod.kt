package net.jidb.to.base.neoforge.mod

import net.jidb.to.base.api.mod.ToPlatformMod
import net.jidb.to.base.neoforge.platform.NetworkingForgePlatformModule
import net.jidb.to.base.neoforge.platform.ReloadListenerForgePlatformModule
import net.jidb.to.base.neoforge.service.ForgeRegisterService
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

/**
 * The Neoforge entry point of a mod, which brings the common mod up as it is constructed and then binds everything this loader defers onto the mod's own buses.
 *
 * See [net.jidb.to.base.neoforge.client.mod.ToForgeClientMod] for the entry point of the client side.
 *
 * @since 0.2.0
 */
abstract class ToForgeMod : ToPlatformMod {

    /**
     * The object holding this mod's handlers for events raised on the game bus, or `null` where it has none. Defaults to `null`.
     *
     * @since 0.2.0
     */
    open val gameEvents: Any? = null

    /**
     * The object holding this mod's handlers for events raised on the mod bus, or `null` where it has none. Defaults to `null`.
     *
     * @since 0.2.0
     */
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

    /**
     * A listener that does nothing, whose only purpose is to give Neoforge's annotation processor something to find, so that the object is constructed at all.
     *
     * @param event The construction event, which is ignored.
     * @since 0.2.0
     */
    abstract fun subscribeStub(event: FMLConstructModEvent)

    /**
     * Sets the mod up once the loader has finished constructing every mod, which is the point at which content other mods registered can be relied on.
     *
     * @param event The setup event, which is ignored.
     * @since 0.2.0
     */
    @Suppress("detekt.UnusedParameter")
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        common.setup()
        setup()
    }

}
