package net.jidb.to.stars.neoforge.client.data

import net.jidb.to.base.client.data.pub.provider.wiki.CompositeWikiDataEnforcer
import net.jidb.to.base.client.data.pub.provider.wiki.ModWikiDataEnforcer
import net.jidb.to.base.client.data.pub.provider.wiki.RegistryWikiDataEnforcer
import net.jidb.to.base.neoforge.client.data.mod.ToForgeDataMod
import net.jidb.to.base.neoforge.data.provider.AutoCopyTagDataProvider
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.content.ToStarsBiomeModLibrary
import net.jidb.to.stars.content.ToStarsGameTestLibrary
import net.jidb.to.stars.neoforge.client.data.content.ToStarsDataLibrary
import net.jidb.to.stars.neoforge.client.data.provider.ToStarsAdvancementDataProvider
import net.jidb.to.stars.neoforge.client.data.provider.ToStarsLanguageDataProvider
import net.jidb.to.stars.neoforge.client.data.provider.ToStarsModelDataProvider
import net.jidb.to.stars.neoforge.client.data.provider.ToStarsParticleDataProvider
import net.jidb.to.stars.neoforge.client.data.provider.ToStarsSoundDataProvider
import net.jidb.to.stars.neoforge.data.content.ToStarsConfiguredFeatureDataLibrary
import net.jidb.to.stars.neoforge.data.content.ToStarsDamageTypeDataLibrary
import net.jidb.to.stars.neoforge.data.content.ToStarsMusicDataLibrary
import net.jidb.to.stars.neoforge.data.content.ToStarsPlacedFeatureDataLibrary
import net.jidb.to.stars.neoforge.data.provider.ToStarsDamageTypeTagDataProvider
import net.jidb.to.stars.neoforge.data.provider.ToStarsItemCopyTagDataProvider
import net.jidb.to.stars.neoforge.data.provider.ToStarsItemTagDataProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture

class ToStarsForgeDataMod(event: GatherDataEvent.Client) : ToForgeDataMod(event) {

    override val collections = ToStarsDataLibrary

    override val language = ::ToStarsLanguageDataProvider
    override val models = listOf(::ToStarsModelDataProvider)
    override val copyTags = listOf(
        { output: PackOutput, lookup: CompletableFuture<HolderLookup.Provider>, blockTags: CompletableFuture<TagsProvider.TagLookup<Block>> -> AutoCopyTagDataProvider(ToStarsMod.blockTags.values.associateBy { it.location }, ToStarsMod.itemTags?.values?.associateBy { it.location } ?: emptyMap(), output, lookup, blockTags, modid) },
        ::ToStarsItemCopyTagDataProvider
    )
    override val advancements = listOf(ToStarsAdvancementDataProvider())
    override val gameTests = ToStarsGameTestLibrary
    override val particles = listOf(::ToStarsParticleDataProvider)
    override val sounds = listOf(::ToStarsSoundDataProvider)
    override val tags = listOf(::ToStarsItemTagDataProvider, ::ToStarsDamageTypeTagDataProvider)

    override val damageTypes = ToStarsDamageTypeDataLibrary
    override val music = ToStarsMusicDataLibrary

    override val configuredFeatures = ToStarsConfiguredFeatureDataLibrary
    override val placedFeatures = ToStarsPlacedFeatureDataLibrary
    override val biomeMods = listOf(ToStarsBiomeModLibrary)

    override val wiki = listOf(CompositeWikiDataEnforcer(RegistryWikiDataEnforcer(ToStarsMod.modid, {
        it.registry() == BuiltInRegistries.CREATIVE_MODE_TAB.key().identifier() || it.registry() == BuiltInRegistries.TEST_FUNCTION.key().identifier() || it.registry().path.contains("recipe") || (it.registry() == Registries.TRIGGER_TYPE.identifier() && it.identifier().path == "race")
    }), ModWikiDataEnforcer(ToStarsMod.modid)))

}
