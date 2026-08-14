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
     * The generator is reached through the [ForgeDataModService] this mod registers rather than named here, since the generator packages an implementation lives in are stripped from a built jar.
     * Every registered implementation is visible, one per mod that generates anything, so this mod's own is picked out by [ForgeDataModService.modid].
     *
     * @param event The event the generators are registered against.
     * @throws IllegalStateException Where the mod registered no data generator service.
     * @since 0.3.0
     */
    fun onGatherData(event: GatherDataEvent.Client) {
        val modid = client.common.modid
        val service = ServiceLoader.load(ForgeDataModService::class.java).firstOrNull { it.modid == modid }
        (service ?: error("Data mod service is not registered for $modid.")).generate(event)
    }

}
