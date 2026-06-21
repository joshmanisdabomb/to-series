package net.jidb.to.base.neoforge.client.data.mod

import net.jidb.to.base.client.data.api.mod.ToContentDataMod
import net.jidb.to.base.data.api.library.DatapackLibrary
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.tags.TagsProvider
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider
import net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider
import net.neoforged.neoforge.common.world.BiomeModifier
import java.util.concurrent.CompletableFuture

interface ToContentForgeDataMod : ToContentDataMod {
    val languages: List<(output: PackOutput) -> LanguageProvider> get() = emptyList()
    val copyTags: List<(output: PackOutput, lookup: CompletableFuture<HolderLookup.Provider>, blockTags: CompletableFuture<TagsProvider.TagLookup<Block>>) -> BlockTagCopyingItemTagProvider> get() = emptyList()
    val recipes: List<(output: PackOutput, lookup: CompletableFuture<HolderLookup.Provider>) -> RecipeProvider.Runner> get() = emptyList()
    val particles: List<(output: PackOutput, lookup: CompletableFuture<HolderLookup.Provider>) -> ParticleDescriptionProvider> get() = emptyList()
    val sounds: List<(output: PackOutput, lookup: CompletableFuture<HolderLookup.Provider>) -> SoundDefinitionsProvider> get() = emptyList()

    val biomeModifiers: DatapackLibrary<BiomeModifier>? get() = null
}