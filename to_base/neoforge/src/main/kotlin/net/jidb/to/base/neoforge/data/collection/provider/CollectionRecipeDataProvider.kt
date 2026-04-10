package net.jidb.to.base.neoforge.data.collection.provider

import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.event.RecipeDataCollectionEvent
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import java.util.concurrent.CompletableFuture

class CollectionRecipeDataProvider(private val collections: Iterable<DataCollection<*>>, registries: HolderLookup.Provider, output: RecipeOutput) : RecipeProvider(registries, output) {

    override fun buildRecipes() {
        val event = RecipeDataCollectionEvent(registries, output)
        event.process(collections)
    }

    class Runner(private val collections: Iterable<DataCollection<*>>, packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>, protected val modid: String) : RecipeProvider.Runner(packOutput, registries) {

        override fun createRecipeProvider(provider: HolderLookup.Provider, output: RecipeOutput) = CollectionRecipeDataProvider(collections, provider, output)

        override fun getName() = "$modid Data Collections: Recipes"

    }

}