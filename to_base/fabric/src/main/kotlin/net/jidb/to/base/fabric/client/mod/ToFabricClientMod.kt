package net.jidb.to.base.fabric.client.mod

import net.fabricmc.api.ClientModInitializer
import net.jidb.to.base.client.api.mod.ToPlatformClientMod
import net.jidb.to.base.fabric.library.FabricEventHandlerLibrary

/**
 * The Fabric client entry point of a mod, which brings the common client mod up and then registers whatever this loader needs on top of it.
 *
 * See [net.jidb.to.base.fabric.mod.ToFabricMod] for the entry point both sides go through.
 *
 * @since 0.2.0
 */
abstract class ToFabricClientMod : ToPlatformClientMod, ClientModInitializer {

    /**
     * The callbacks this mod registers against Fabric's own client events, or `null` where it registers none. Defaults to `null`.
     *
     * @since 0.8.0
     */
    open val events: FabricEventHandlerLibrary? = null

    override fun onInitializeClient() {
        client.clientInit()
        clientInit()

        events?.build()

        client.clientSetup()
        clientSetup()
    }

}
