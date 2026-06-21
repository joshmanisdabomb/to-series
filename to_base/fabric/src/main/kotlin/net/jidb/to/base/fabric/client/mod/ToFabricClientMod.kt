package net.jidb.to.base.fabric.client.mod

import net.fabricmc.api.ClientModInitializer
import net.jidb.to.base.client.api.mod.ToPlatformClientMod

abstract class ToFabricClientMod : ToPlatformClientMod, ClientModInitializer {

    override fun onInitializeClient() {
        client.clientInit()
        clientInit()

        client.clientSetup()
        clientSetup()
    }

}