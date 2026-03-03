package net.jidb.to.stars.neoforge

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.data.provider.CopyDataProvider
import net.jidb.to.base.data.provider.DeleteDataProvider
import net.jidb.to.base.neoforge.ToBaseForgeMod
import net.jidb.to.base.neoforge.service.ForgeRegisterService
import net.jidb.to.stars.ToSkyAndStarsMod
import net.jidb.to.stars.neoforge.data.ToSkyAndStarsLanguageDataProvider
import net.jidb.to.stars.neoforge.data.ToSkyAndStarsModelDataProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.neoforged.neoforge.data.event.GatherDataEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.nio.file.Path

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

    @SubscribeEvent
    fun onGatherData(event: GatherDataEvent.Client) {
        event.createProvider(::ToSkyAndStarsLanguageDataProvider)
        event.createProvider(::ToSkyAndStarsModelDataProvider)

        event.createProvider { DeleteDataProvider(it) { output ->
            val resources = Path.of(output.toString().replace("neoforge", "common").replace("generated", "resources"))
            val assets = resources.resolve("assets/${ToSkyAndStarsMod.MOD_ID}/")
            listOf("blockstates", "models", "lang").map(assets::resolve)
        } }
        event.createProvider { CopyDataProvider(it) { output ->
            Path.of(output.toString().replace("neoforge", "common").replace("generated", "resources"))
        } }
    }
}
