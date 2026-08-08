package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.recipe.processor.ProcessorRecipe
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeSerializer

/**
 * [SimpleRegistryLibrary] implementation holding how each of this mod's recipes is read and written.
 */
object ToStarsRecipeSerializerLibrary : SimpleRegistryLibrary<RecipeSerializer<*>>(ToStarsMod.modid) {

    override val registry = BuiltInRegistries.RECIPE_SERIALIZER

    /**
     * How a processor recipe is read and written.
     */
    val processor by this { RecipeSerializer(ProcessorRecipe.codec, ProcessorRecipe.streamCodec) }

}
