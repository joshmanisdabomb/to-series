package net.jidb.to.stars.recipe.processor

import net.minecraft.world.item.crafting.Recipe

/**
 * Where a processor recipe sits in the recipe book, i.e. its section and which other recipes it is shown alongside.
 *
 * @property category The section it is filed under.
 * @property group The recipes it is grouped with, which the book shows as one entry that cycles through them.
 */
data class ProcessorRecipeBookInfo(val category: ProcessorRecipeBookCategory, val group: String) : Recipe.BookInfo<ProcessorRecipeBookCategory> {

    override fun category() = category

    override fun group() = group

    companion object {

        /**
         * The codec this is read from a recipe file through.
         */
        val mapCodec = Recipe.BookInfo.mapCodec(ProcessorRecipeBookCategory.codec, ProcessorRecipeBookCategory.MISC, ::ProcessorRecipeBookInfo)

        /**
         * The codec this is sent to the client through.
         */
        val streamCodec = Recipe.BookInfo.streamCodec(ProcessorRecipeBookCategory.streamCodec, ::ProcessorRecipeBookInfo)

    }

}
