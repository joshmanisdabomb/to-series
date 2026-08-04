package net.jidb.to.base.neoforge.client.data.mod

import net.jidb.to.base.client.data.api.mod.ToContentDataMod
import net.jidb.to.base.data.api.library.DatapackLibrary
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.tags.TagsProvider
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.item.JukeboxSong
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider
import net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider
import net.neoforged.neoforge.common.world.BiomeModifier
import java.util.concurrent.CompletableFuture

/**
 * The providers a mod generates through that only Neoforge has, added to the ones [ToContentDataMod] names for both loaders.
 *
 * Everything here is optional, since a mod only names the kinds of data it actually has.
 *
 * @since 0.3.0
 */
interface ToContentForgeDataMod : ToContentDataMod {

    /**
     * Builds the providers writing out translations through Neoforge's own language provider, alongside the multi-locale one both loaders share. Defaults to none.
     *
     * @since 0.3.0
     */
    val languages: List<(output: PackOutput) -> LanguageProvider> get() = emptyList()

    /**
     * Builds the providers copying a block tag onto the items of those blocks, so that the pair need not be written out twice. Defaults to none.
     *
     * @since 0.3.0
     */
    val copyTags: List<(output: PackOutput, lookup: CompletableFuture<HolderLookup.Provider>, blockTags: CompletableFuture<TagsProvider.TagLookup<Block>>) -> BlockTagCopyingItemTagProvider> get() = emptyList()

    /**
     * Builds the providers writing out the recipes of this mod that no collection describes. Defaults to none.
     *
     * @since 0.3.0
     */
    val recipes: List<(output: PackOutput, lookup: CompletableFuture<HolderLookup.Provider>) -> RecipeProvider.Runner> get() = emptyList()

    /**
     * Builds the providers writing out the particle descriptions of this mod, i.e. which sprites each particle is drawn from. Defaults to none.
     *
     * @since 0.3.0
     */
    val particles: List<(output: PackOutput, lookup: CompletableFuture<HolderLookup.Provider>) -> ParticleDescriptionProvider> get() = emptyList()

    /**
     * Builds the providers writing out the sound definitions of this mod. Defaults to none.
     *
     * @since 0.3.0
     */
    val sounds: List<(output: PackOutput, lookup: CompletableFuture<HolderLookup.Provider>) -> SoundDefinitionsProvider> get() = emptyList()

    /**
     * The library holding the biome modifiers of this mod, or `null` where it has none. Defaults to `null`.
     *
     * Neoforge expresses a biome modifier as a data pack file, which is why it is generated here rather than registered like Fabric's.
     *
     * @since 0.3.0
     */
    val biomeModifiers: DatapackLibrary<BiomeModifier>? get() = null

    /**
     * The library holding the damage types of this mod, or `null` where it has none. Defaults to `null`.
     *
     * @since 0.3.0
     */
    val damageTypes: DatapackLibrary<DamageType>? get() = null

    /**
     * The library holding the jukebox songs of this mod, or `null` where it has none. Defaults to `null`.
     *
     * @since 0.3.0
     */
    val music: DatapackLibrary<JukeboxSong>? get() = null

}
