package net.jidb.to.stars.neoforge.data.provider

import net.jidb.to.base.ToBaseMod
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ItemTagsProvider
import java.util.concurrent.CompletableFuture

/**
 * Generates the item tags of this mod that no collection describes.
 *
 * @param output Where the generated files are written.
 * @param provider The registries the tags are built against.
 */
class ToStarsItemTagDataProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) : ItemTagsProvider(output, provider, ToBaseMod.modid) {

    override fun addTags(provider: HolderLookup.Provider) {
        this.tag(ToStarsMod.itemTags.heat_generator_2x)
            .addTag(ItemTags.COALS)
            .addTag(Tags.Items.STORAGE_BLOCKS_COAL)

        this.tag(ToStarsMod.itemTags.machine_enclosures)
            .addTag(ToStarsMod.itemTags.tier_1_machine_enclosures)
            .addTag(ToStarsMod.itemTags.tier_2_machine_enclosures)

        this.tag(ToStarsMod.itemTags.batteries)
            .addTag(ToStarsMod.itemTags.tier_1_batteries)
            .addTag(ToStarsMod.itemTags.tier_2_batteries)

        this.tag(ToStarsMod.itemTags.power_banks)
            .addTag(ToStarsMod.itemTags.tier_1_power_banks)
            .addTag(ToStarsMod.itemTags.tier_2_power_banks)

        this.tag(ToStarsMod.itemTags.heat_generators)
            .addTag(ToStarsMod.itemTags.solid_generators)
        this.tag(ToStarsMod.itemTags.solid_generators)
            .addTag(ToStarsMod.itemTags.tier_1_solid_generators)
            .addTag(ToStarsMod.itemTags.tier_2_solid_generators)

        this.tag(ToStarsMod.itemTags.turbines)
            .addTag(ToStarsMod.itemTags.tier_1_turbines)
            .addTag(ToStarsMod.itemTags.tier_2_turbines)

        this.tag(ToStarsMod.itemTags.centrifuges)
            .addTag(ToStarsMod.itemTags.tier_1_centrifuges)
            .addTag(ToStarsMod.itemTags.tier_2_centrifuges)

        this.tag(ToStarsMod.itemTags.powered_smelters)
            .addTag(ToStarsMod.itemTags.powered_furnaces)
            .addTag(ToStarsMod.itemTags.powered_specialised_smelters)
        this.tag(ToStarsMod.itemTags.powered_specialised_smelters)
            .addTag(ToStarsMod.itemTags.powered_smokers)
            .addTag(ToStarsMod.itemTags.powered_blast_furnaces)
            .addTag(ToStarsMod.itemTags.powered_kilns)
        this.tag(ToStarsMod.itemTags.powered_furnaces)
            .addTag(ToStarsMod.itemTags.tier_1_powered_furnaces)
            .addTag(ToStarsMod.itemTags.tier_2_powered_furnaces)
        this.tag(ToStarsMod.itemTags.powered_smokers)
            .addTag(ToStarsMod.itemTags.tier_1_powered_smokers)
            .addTag(ToStarsMod.itemTags.tier_2_powered_smokers)
        this.tag(ToStarsMod.itemTags.powered_blast_furnaces)
            .addTag(ToStarsMod.itemTags.tier_1_powered_blast_furnaces)
            .addTag(ToStarsMod.itemTags.tier_2_powered_blast_furnaces)
        this.tag(ToStarsMod.itemTags.powered_kilns)
            .addTag(ToStarsMod.itemTags.tier_1_powered_kilns)
            .addTag(ToStarsMod.itemTags.tier_2_powered_kilns)
    }

}
