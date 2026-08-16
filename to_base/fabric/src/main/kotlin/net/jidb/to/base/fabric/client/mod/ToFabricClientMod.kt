package net.jidb.to.base.fabric.client.mod

import net.fabricmc.api.ClientModInitializer
import net.jidb.to.base.client.api.mod.ToPlatformClientMod
import net.jidb.to.base.fabric.library.FabricEventHandlerLibrary

abstract class ToFabricClientMod : ToPlatformClientMod, ClientModInitializer {

    open val events: FabricEventHandlerLibrary? = null

    override fun onInitializeClient() {
        client.clientInit()
        clientInit()

        events?.build()

        client.clientSetup()
        clientSetup()
    }

}
