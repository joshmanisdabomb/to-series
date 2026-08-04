package net.jidb.to.base.data.api.collection.event

import net.jidb.to.base.data.api.ToDataRecipeHelper
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput

/**
 * The [DataCollectionEvent] generating the recipes of a mod.
 * Nothing is merged here, as a module writes each recipe straight to [output]; the result type is [Unit] and only records that something was generated at all.
 *
 * @param registries The registries the recipes are built against.
 * @property output The output each generated recipe is written to.
 * @since 0.3.0
 */
class RecipeDataCollectionEvent(registries: HolderLookup.Provider, val output: RecipeOutput) : DataCollectionEvent<Unit, Unit, Unit>() {

    /**
     * The item registry, which a recipe's ingredients and result are looked up in.
     *
     * @since 0.3.0
     */
    val registry = registries.lookupOrThrow(Registries.ITEM)

    /**
     * The helper offering the pieces a recipe usually needs, such as the unlock criterion for an ingredient.
     *
     * @since 0.3.0
     */
    val helper = ToDataRecipeHelper(registry)

    override fun combineFromModules(results: Iterable<Unit>): Unit? {
        if (results.count() <= 0) return null
        return Unit
    }

    override fun combineFromCollections(results: Iterable<Unit>) = combineFromModules(results)

}
