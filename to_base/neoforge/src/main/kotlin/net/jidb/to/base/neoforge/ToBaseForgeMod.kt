package net.jidb.to.base.neoforge

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.data.ToBaseData
import net.jidb.to.base.data.provider.CopyDataProvider
import net.jidb.to.base.data.provider.DeleteDataProvider
import net.jidb.to.base.data.provider.WikiDataProvider
import net.jidb.to.base.data.provider.wiki.RegistryWikiDataEnforcer
import net.jidb.to.base.neoforge.content.data.*
import net.jidb.to.base.neoforge.mod.ToForgeMod
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.isDirectory

@Mod(ToBaseMod.MOD_ID)
@EventBusSubscriber
object ToBaseForgeMod : ToForgeMod() {
    override val common get() = ToBaseMod

    @SubscribeEvent
    fun onGatherData(event: GatherDataEvent.Client) {
        val resources = Path.of(event.generator.packOutput.outputFolder.toString().replace("neoforge", "common").replace("generated", "resources"))

        event.createProvider { DeleteDataProvider(it, ToBaseData.getReplaceableResources(resources)) }

        event.createProvider { CopyDataProvider(it, resources.parent / "templates" / "models", it.outputFolder / "assets" / ToBaseMod.modid / "models") { dst, src ->
            if (!src.isDirectory()) dst.resolveSibling("template_" + dst.fileName.toString()) else dst
        } }

        event.createProvider(::ToBaseEnglishLanguageDataProvider)
        event.createProvider(::ToBaseModelDataProvider)
        event.createProvider(::ToBaseBlockTagDataProvider)
        event.createProvider(::ToBaseItemTagDataProvider)
        event.createProvider { output, lookupProvider -> LootTableProvider(output, mutableSetOf(), listOf(
            SubProviderEntry(::ToBaseBlockLootDataProvider, LootContextParamSets.BLOCK)
        ), lookupProvider) }
        event.createProvider(ToBaseRecipeDataProvider::Runner)
        event.createProvider { WikiDataProvider(it, resources.parent / "templates" / "wiki").enforce(RegistryWikiDataEnforcer(ToBaseMod.modid)) }

        event.createProvider { CopyDataProvider(it, it.outputFolder, resources) }
    }
}
