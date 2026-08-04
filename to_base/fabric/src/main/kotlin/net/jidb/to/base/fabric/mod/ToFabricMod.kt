package net.jidb.to.base.fabric.mod

import net.fabricmc.api.ModInitializer
import net.jidb.to.base.api.mod.ToPlatformMod
import net.jidb.to.base.fabric.library.FabricEventHandlerLibrary

/**
 * The Fabric entry point of a mod, which brings the common mod up and then registers whatever this loader needs on top of it.
 *
 * See [net.jidb.to.base.fabric.client.mod.ToFabricClientMod] for the entry point of the client side.
 *
 * @since 0.2.0
 */
abstract class ToFabricMod : ToPlatformMod, ModInitializer {

    /**
     * The callbacks this mod registers against Fabric's own events, or `null` where it registers none. Defaults to `null`.
     *
     * @since 0.6.0
     */
    open val events: FabricEventHandlerLibrary? = null

    override fun onInitialize() {
        common.init()
        init()

        events?.build()

        common.setup()
        setup()
    }

}
