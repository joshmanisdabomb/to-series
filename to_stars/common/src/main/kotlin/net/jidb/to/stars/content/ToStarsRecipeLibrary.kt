package net.jidb.to.stars.content

import net.jidb.to.base.pub.library.RecipeTypeLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.recipe.processor.ProcessorRecipe

/**
 * [RecipeTypeLibrary] implementation holding the recipe types of this mod.
 */
object ToStarsRecipeLibrary : RecipeTypeLibrary(ToStarsMod.modid) {

    /**
     * A recipe run by a processor, i.e. a centrifuge.
     */
    val processor by this<ProcessorRecipe>()

}
