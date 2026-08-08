package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.recipe.processor.ProcessorRecipeDisplay
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.display.RecipeDisplay

/**
 * [SimpleRegistryLibrary] implementation holding how each of this mod's recipes is shown in the recipe book.
 */
object ToStarsRecipeDisplayLibrary : SimpleRegistryLibrary<RecipeDisplay.Type<*>>(ToStarsMod.modid) {

    override val registry = BuiltInRegistries.RECIPE_DISPLAY

    /**
     * How a processor recipe is shown.
     */
    val processor by this { RecipeDisplay.Type(ProcessorRecipeDisplay.codec, ProcessorRecipeDisplay.streamCodec) }

}
