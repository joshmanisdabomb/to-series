package net.jidb.to.stars.neoforge

import net.jidb.to.base.data.ToBaseData
import net.jidb.to.base.data.provider.CopyDataProvider
import net.jidb.to.base.data.provider.DeleteDataProvider
import net.jidb.to.base.data.provider.WikiDataProvider
import net.jidb.to.base.data.provider.wiki.RegistryWikiDataEnforcer
import net.jidb.to.base.neoforge.mod.ToForgeMod
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.neoforge.data.*
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.nio.file.Path
import kotlin.io.path.div

@Mod(ToStarsMod.MOD_ID)
@EventBusSubscriber
object ToStarsForgeMod : ToForgeMod() {
    override val common get() = ToStarsMod

    @SubscribeEvent
    fun onGatherData(event: GatherDataEvent.Client) {
        val resources = Path.of(event.generator.packOutput.outputFolder.toString().replace("neoforge", "common").replace("generated", "resources"))

        event.createProvider { DeleteDataProvider(it, ToBaseData.getReplaceableResources(resources)) }

        event.createProvider(::ToStarsEnglishLanguageDataProvider)
        event.createProvider(::ToStarsModelDataProvider)
        event.createProvider(::ToStarsBlockTagDataProvider)
        event.createProvider { output, lookupProvider -> LootTableProvider(output, mutableSetOf(), listOf(
            SubProviderEntry(::ToStarsBlockLootDataProvider, LootContextParamSets.BLOCK)
        ), lookupProvider) }
        event.createProvider(::ToStarsParticleDataProvider)
        event.createProvider(::ToStarsSoundDataProvider)
        event.createProvider { WikiDataProvider(it, resources.parent / "templates" / "wiki").enforce(RegistryWikiDataEnforcer(ToStarsMod.modid, {
            it.registry() == BuiltInRegistries.CREATIVE_MODE_TAB.key().identifier()
        })) }

        event.createProvider { CopyDataProvider(it, it.outputFolder,  resources) }
    }
}
