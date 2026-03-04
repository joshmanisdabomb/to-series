package net.jidb.to.base.neoforge

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.data.provider.CopyDataProvider
import net.jidb.to.base.data.provider.DeleteDataProvider
import net.jidb.to.base.data.provider.WikiDataProvider
import net.jidb.to.base.data.provider.wiki.RegistryWikiDataEnforcer
import net.jidb.to.base.neoforge.content.data.ToBaseLanguageDataProvider
import net.jidb.to.base.neoforge.content.data.ToBaseModelDataProvider
import net.jidb.to.base.neoforge.service.ForgeRegisterService
import net.minecraft.core.registries.BuiltInRegistries
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.neoforged.neoforge.data.event.GatherDataEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.nio.file.Path
import kotlin.io.path.div

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
        val resources = Path.of(event.generator.packOutput.outputFolder.toString().replace("neoforge", "common").replace("generated", "resources"))

        event.createProvider(::ToBaseLanguageDataProvider)
        event.createProvider(::ToBaseModelDataProvider)
        event.createProvider { WikiDataProvider(it, resources.parent / "templates" / "wiki").enforce(RegistryWikiDataEnforcer(ToBaseMod.MOD_ID) { key ->
            key.registry() == BuiltInRegistries.CREATIVE_MODE_TAB.key().identifier()
        }) }

        event.createProvider { DeleteDataProvider(it, listOf("blockstates", "models", "lang", "wiki").map { resources / "assets" / ToBaseMod.MOD_ID / it }) }
        event.createProvider { CopyDataProvider(it, resources) }
    }
}
