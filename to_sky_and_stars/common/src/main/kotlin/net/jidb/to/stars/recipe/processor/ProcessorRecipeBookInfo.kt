package net.jidb.to.stars.recipe.processor

import net.minecraft.world.item.crafting.Recipe


data class ProcessorRecipeBookInfo(val category: ProcessorRecipeBookCategory, val group: String) : Recipe.BookInfo<ProcessorRecipeBookCategory> {

    override fun category() = category

    override fun group() = group

    companion object {
        val mapCodec = Recipe.BookInfo.mapCodec(ProcessorRecipeBookCategory.codec, ProcessorRecipeBookCategory.MISC, ::ProcessorRecipeBookInfo)
        val streamCodec = Recipe.BookInfo.streamCodec(ProcessorRecipeBookCategory.streamCodec, ::ProcessorRecipeBookInfo)
    }

}
