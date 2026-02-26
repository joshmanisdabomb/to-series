package net.jidb.to.stars.neoforge

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.neoforge.ToBaseForgeMod
import net.jidb.to.stars.ToSkyAndStarsMod
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent

@Mod(ToSkyAndStarsMod.mod_id)
@EventBusSubscriber
object ToSkyAndStarsForgeMod {
    init {
        ToSkyAndStarsMod.init()
        ToSkyAndStarsMod.logger.info("Hello world from Forge!")

        ToBaseMod.logger.info("Hello ${ToBaseMod.mod_id} from forge ${ToSkyAndStarsMod.mod_id}!")
        ToBaseMod.logger.info("Base forge mod class: $ToBaseForgeMod")
    }

    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        ToSkyAndStarsMod.logger.info("Hello! This is working!")
    }

    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        ToSkyAndStarsMod.logger.info("Initializing client...")
    }

    @SubscribeEvent
    fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        ToSkyAndStarsMod.logger.info("Server starting...")
    }
}
