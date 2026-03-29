package net.jidb.to.base.neoforge.content.data

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.content.ToBaseItemTagLibrary
import net.jidb.to.base.service.Services
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import java.util.concurrent.CompletableFuture

class ToBaseRecipeDataProvider(registries: HolderLookup.Provider, output: RecipeOutput) : RecipeProvider(registries, output) {

    override fun buildRecipes() {
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ToBaseMod.blocks.research_desk)
            .pattern("bpi")
            .pattern("SSS")
            .pattern("W B")
            .define('W', ItemTags.PLANKS)
            .define('S', ItemTags.WOODEN_SLABS)
            .define('B', Services.platform.tags.getCommonItem("bookshelves")!!)
            .define('b', Items.BOOK)
            .define('p', Items.PAPER)
            .define('i', Items.INK_SAC)
            .unlockedBy("has_starter", this.has(ToBaseItemTagLibrary.research_desk_unlock))
            .save(this.output)
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : RecipeProvider.Runner(packOutput, registries) {

        override fun createRecipeProvider(provider: HolderLookup.Provider, output: RecipeOutput) = ToBaseRecipeDataProvider(provider, output)

        override fun getName() = "To Lay the Foundations Recipes"

    }

}