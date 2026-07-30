package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.recipe.processor.ProcessorRecipe
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeSerializer

object ToStarsRecipeSerializerLibrary : SimpleRegistryLibrary<RecipeSerializer<*>>(ToStarsMod.modid) {

    override val registry = BuiltInRegistries.RECIPE_SERIALIZER

    val processor by this { RecipeSerializer(ProcessorRecipe.codec, ProcessorRecipe.streamCodec) }

}
