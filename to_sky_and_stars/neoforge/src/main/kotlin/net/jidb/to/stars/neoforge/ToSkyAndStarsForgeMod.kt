package net.jidb.to.stars.neoforge

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.neoforge.ToBaseForgeMod
import net.jidb.to.base.neoforge.service.ForgeRegisterService
import net.jidb.to.stars.ToSkyAndStarsMod
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(ToSkyAndStarsMod.MOD_ID)
@EventBusSubscriber
object ToSkyAndStarsForgeMod {
    init {
        ToSkyAndStarsMod.init()
        ToSkyAndStarsMod.logger.info("Hello world from Forge!")

        ToBaseMod.logger.info("Hello ${ToBaseMod.MOD_ID} from forge ${ToSkyAndStarsMod.MOD_ID}!")
        ToBaseMod.logger.info("Base forge mod class: $ToBaseForgeMod")

        ForgeRegisterService.registerMod(ToSkyAndStarsMod.MOD_ID, MOD_BUS)
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
