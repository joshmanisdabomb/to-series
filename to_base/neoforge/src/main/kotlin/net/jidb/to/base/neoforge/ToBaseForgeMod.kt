package net.jidb.to.base.neoforge

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.data.provider.CopyDataProvider
import net.jidb.to.base.data.provider.DeleteDataProvider
import net.jidb.to.base.data.provider.WikiDataProvider
import net.jidb.to.base.data.provider.wiki.RegistryWikiDataEnforcer
import net.jidb.to.base.neoforge.content.data.ToBaseEnglishLanguageDataProvider
import net.jidb.to.base.neoforge.content.data.ToBaseItemTagDataProvider
import net.jidb.to.base.neoforge.content.data.ToBaseModelDataProvider
import net.jidb.to.base.neoforge.content.data.ToBaseRecipeDataProvider
import net.jidb.to.base.neoforge.service.ForgeRegisterService
import net.minecraft.core.registries.BuiltInRegistries
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.neoforged.neoforge.data.event.GatherDataEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.isDirectory


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
        ToBaseMod.blocks.properties.build()
    }

    @SubscribeEvent
    fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        ToBaseMod.logger.info("Server starting...")
    }

    @SubscribeEvent
    fun onGatherData(event: GatherDataEvent.Client) {
        val resources = Path.of(event.generator.packOutput.outputFolder.toString().replace("neoforge", "common").replace("generated", "resources"))

        event.createProvider { CopyDataProvider(it, resources.parent / "templates" / "models", it.outputFolder / "assets" / ToBaseMod.MOD_ID / "models") { dst, src ->
            if (!src.isDirectory()) dst.resolveSibling("template_" + dst.fileName.toString()) else dst
        } }

        event.createProvider(::ToBaseEnglishLanguageDataProvider)
        event.createProvider(::ToBaseModelDataProvider)
        event.createProvider(::ToBaseItemTagDataProvider)
        event.createProvider(ToBaseRecipeDataProvider::Runner)
        event.createProvider { WikiDataProvider(it, resources.parent / "templates" / "wiki").enforce(RegistryWikiDataEnforcer(ToBaseMod.MOD_ID) { key ->
            key.registry() == BuiltInRegistries.CREATIVE_MODE_TAB.key().identifier() || key.registry() == BuiltInRegistries.MENU.key().identifier()
        }) }

        event.createProvider { DeleteDataProvider(it, listOf("blockstates", "models", "lang", "wiki").map { resources / "assets" / ToBaseMod.MOD_ID / it }) }
        event.createProvider { CopyDataProvider(it, it.outputFolder, resources) }
    }
}
