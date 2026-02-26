package net.jidb.to.stars.fabric.client

import net.fabricmc.api.ClientModInitializer

object ToSkyAndStarsFabricClientMod : ClientModInitializer {
    override fun onInitializeClient() {
        println("fabric client")
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }
}
