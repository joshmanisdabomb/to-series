package net.jidb.to.base.neoforge.data.collection.provider

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.RecipeDataCollectionEvent
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import java.util.concurrent.CompletableFuture

/**
 * The [RecipeProvider] that generates the recipes described by a mod's collections.
 *
 * @param collections The collections being generated from.
 * @param registries The registries the recipes are built against.
 * @param output Where the generated recipes are written.
 * @since 0.3.0
 */
class CollectionRecipeDataProvider(private val collections: Iterable<DataCollection<*>>, registries: HolderLookup.Provider, output: RecipeOutput) : RecipeProvider(registries, output) {

    override fun buildRecipes() {
        val event = RecipeDataCollectionEvent(registries, output)
        event.process(collections)
    }

    /**
     * The runner that builds the provider once the registries have loaded, which is how vanilla's recipe providers are declared.
     *
     * @param collections The collections being generated from.
     * @param packOutput Where the generated files are written.
     * @param registries The registries the recipes are built against.
     * @property modid The mod ID the files are written under.
     * @since 0.3.0
     */
    class Runner(private val collections: Iterable<DataCollection<*>>, packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>, protected val modid: String) : RecipeProvider.Runner(packOutput, registries) {

        override fun createRecipeProvider(provider: HolderLookup.Provider, output: RecipeOutput) = CollectionRecipeDataProvider(collections, provider, output)

        override fun getName() = "$modid Data Collections: Recipes"

    }

}
