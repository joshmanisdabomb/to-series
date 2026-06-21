package net.jidb.to.base.data.api.collection.event

import net.jidb.to.base.data.api.ToDataRecipeHelper
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput

class RecipeDataCollectionEvent(registries: HolderLookup.Provider, val output: RecipeOutput) : DataCollectionEvent<Unit, Unit, Unit>() {

    val registry = registries.lookupOrThrow(Registries.ITEM)
    val helper = ToDataRecipeHelper(registry)

    override fun combineFromModules(results: Iterable<Unit>): Unit? {
        if (results.count() <= 0) return null
        return Unit
    }

    override fun combineFromCollections(results: Iterable<Unit>) = combineFromModules(results)

}
