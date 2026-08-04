package net.jidb.to.base.neoforge.client.mod

import net.jidb.to.base.client.api.mod.ToPlatformClientMod
import net.jidb.to.base.neoforge.client.data.mod.ToForgeDataMod
import net.jidb.to.base.neoforge.client.platform.BlocksForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.platform.EntitiesForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.platform.ModelsForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.platform.NetworkingForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.platform.ParticleForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.platform.ReloadListenerForgeClientPlatformModule
import net.jidb.to.base.neoforge.client.platform.ScreenForgeClientPlatformModule
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.neoforge.data.event.GatherDataEvent
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

/**
 * The Neoforge client entry point of a mod, which brings the common client mod up as it is constructed and then binds everything the client platform defers onto the mod's own buses.
 *
 * See [net.jidb.to.base.neoforge.mod.ToForgeMod] for the entry point both sides go through.
 *
 * @since 0.2.0
 */
abstract class ToForgeClientMod : ToPlatformClientMod {

    /**
     * The object holding this mod's handlers for client events raised on the game bus, or `null` where it has none. Defaults to `null`.
     *
     * @since 0.2.0
     */
    open val eventHandler: Any? = null

    /**
     * Builds this mod's data generator, given the event it is being run from, or `null` where it generates nothing. Defaults to `null`.
     *
     * @since 0.3.0
     */
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
        ModelsForgeClientPlatformModule.layerRegistry.addListener(client.common.modid, MOD_BUS)
        ModelsForgeClientPlatformModule.specialRegistry.addListener(client.common.modid, MOD_BUS)
        ModelsForgeClientPlatformModule.blockTintRegistry.addListener(client.common.modid, MOD_BUS)
        ModelsForgeClientPlatformModule.itemTintRegistry.addListener(client.common.modid, MOD_BUS)
    }

    /**
     * A listener that does nothing, whose only purpose is to give Neoforge's annotation processor something to find, so that the object is constructed at all.
     *
     * @param event The construction event, which is ignored.
     * @since 0.2.0
     */
    abstract fun subscribeStub(event: FMLConstructModEvent)

    /**
     * Sets the client side up once the loader has finished constructing every mod.
     *
     * @param event The setup event, which is ignored.
     * @since 0.2.0
     */
    @Suppress("detekt.UnusedParameter")
    fun onClientSetup(event: FMLClientSetupEvent) {
        client.clientSetup()
        clientSetup()
    }

    /**
     * Runs this mod's data generators, which only happens on the data generation run rather than in the game itself.
     *
     * @param event The event the generators are registered against.
     * @throws IllegalStateException Where the mod declared no data generator.
     * @since 0.3.0
     */
    fun onGatherData(event: GatherDataEvent.Client) {
        val getter = data ?: error("Data mod is not set.")
        val data = getter(event)
        data.generate()
    }

}
