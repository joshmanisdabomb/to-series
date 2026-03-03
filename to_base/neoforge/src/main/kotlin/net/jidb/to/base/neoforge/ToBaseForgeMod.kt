package net.jidb.to.base.neoforge

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.data.provider.CopyDataProvider
import net.jidb.to.base.data.provider.DeleteDataProvider
import net.jidb.to.base.neoforge.content.data.ToBaseLanguageDataProvider
import net.jidb.to.base.neoforge.content.data.ToBaseModelDataProvider
import net.jidb.to.base.neoforge.service.ForgeRegisterService
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.neoforged.neoforge.data.event.GatherDataEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.nio.file.Path

@Mod(ToBaseMod.MOD_ID)
@EventBusSubscriber
object ToBaseForgeMod {
    init {
        ToBaseMod.init()
        ToBaseMod.logger.info("Hello world from Forge!")

        ForgeRegisterService.registerMod(ToBaseMod.MOD_ID, MOD_BUS)
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

    @SubscribeEvent
    fun onGatherData(event: GatherDataEvent.Client) {
        event.createProvider(::ToBaseLanguageDataProvider)
        event.createProvider(::ToBaseModelDataProvider)

        event.createProvider { DeleteDataProvider(it) { output ->
            val resources = Path.of(output.toString().replace("neoforge", "common").replace("generated", "resources"))
            val assets = resources.resolve("assets/${ToBaseMod.MOD_ID}/")
            listOf("blockstates", "models", "lang").map(assets::resolve)
        } }
        event.createProvider { CopyDataProvider(it) { output ->
            Path.of(output.toString().replace("neoforge", "common").replace("generated", "resources"))
        } }
    }
}
