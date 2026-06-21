package net.jidb.to.base.fabric.mod

import net.fabricmc.api.ModInitializer
import net.jidb.to.base.api.mod.ToPlatformMod
import net.jidb.to.base.fabric.library.FabricEventHandlerLibrary

abstract class ToFabricMod : ToPlatformMod, ModInitializer {
    open val events: FabricEventHandlerLibrary? = null

    override fun onInitialize() {
        common.init()
        init()

        events?.build()

        common.setup()
        setup()
    }
}