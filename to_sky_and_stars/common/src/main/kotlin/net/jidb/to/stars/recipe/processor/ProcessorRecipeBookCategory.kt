package net.jidb.to.stars.recipe.processor

import net.jidb.to.stars.ToStarsMod
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.util.ByIdMap
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.crafting.RecipeBookCategory

/**
 * Enum that defines which section of the recipe book a processor recipe is filed under.
 *
 * @param category The registered category, which is fetched lazily because the registry is not loaded when this enum is.
 */
enum class ProcessorRecipeBookCategory(private val category: () -> RecipeBookCategory) : StringRepresentable {

    /**
     * The one section every processor recipe currently falls into.
     */
    MISC({ ToStarsMod.recipeCategories.processor_misc });

    /**
     * The registered category this stands for.
     *
     * @return The recipe book category.
     */
    fun get() = category()

    override fun getSerializedName() = name.lowercase()

    companion object {

        /**
         * The codec a category is read from a recipe file through, by its own name.
         */
        val codec = StringRepresentable.fromEnum(::values)

        /**
         * The categories by their position in this enum, out of which anything unrecognised reads back as the first.
         */
        val byId = ByIdMap.continuous(ProcessorRecipeBookCategory::ordinal, entries.toTypedArray(), ByIdMap.OutOfBoundsStrategy.ZERO)

        /**
         * The codec a category is sent to the client through, by its position in this enum.
         */
        val streamCodec = ByteBufCodecs.idMapper(byId, ProcessorRecipeBookCategory::ordinal)

    }

}
