package net.jidb.to.base.fabric.mod

import net.fabricmc.api.ModInitializer
import net.jidb.to.base.mod.ToPlatformMod

abstract class ToFabricMod : ToPlatformMod, ModInitializer {
    override fun onInitialize() {
        common.init()
        init()

        common.setup()
        setup()
    }
}