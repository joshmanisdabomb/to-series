package net.jidb.to.base.neoforge

import net.jidb.to.base.ToBaseMod
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent

@Mod(ToBaseMod.mod_id)
@EventBusSubscriber
object ToBaseForgeMod {
    init {
        ToBaseMod.init()
        ToBaseMod.logger.info("Hello world from Forge!")
    }

    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        ToBaseMod.logger.info("Hello! This is working!")
    }

    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        ToBaseMod.logger.info("Initializing client...")
    }

    @SubscribeEvent
    fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        ToBaseMod.logger.info("Server starting...")
    }
}
