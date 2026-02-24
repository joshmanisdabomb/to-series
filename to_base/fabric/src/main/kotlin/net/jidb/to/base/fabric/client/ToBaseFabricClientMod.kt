package net.jidb.to.base.fabric.client

import net.fabricmc.api.ClientModInitializer

object ToBaseFabricClientMod : ClientModInitializer {
    override fun onInitializeClient() {
        println("fabric client")
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }
}
