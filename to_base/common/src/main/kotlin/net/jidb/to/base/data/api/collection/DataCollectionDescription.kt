package net.jidb.to.base.data.api.collection

import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.resources.ResourceKey

/**
 * A declaration of which registry entries a set of data generation modules applies to, and what those modules are.
 * A description is the recipe rather than the result: it is written once in a [net.jidb.to.base.data.api.library.DataCollectionLibrary] and turned into a [DataCollection] per matching entry when the library is built.
 *
 * Modules come in two kinds. An ordinary module always runs, and a default module runs only where no ordinary module has answered for the same kind of data, so the boilerplate a piece of content would otherwise be given can be overridden one part at a time.
 *
 * Every builder function returns the description, so a description is written as a chain.
 *
 * @param affects The predicates deciding which registry entries this description applies to.
 * @since 0.3.0
 */
open class DataCollectionDescription(vararg affects: (key: ResourceKey<*>) -> Boolean) {

    /**
     * The predicates deciding which registry entries this description applies to.
     *
     * @since 0.3.0
     */
    val affects: Set<(key: ResourceKey<*>) -> Boolean> field = affects.toMutableSet()

    /**
     * The factories building the modules that always run, each of which may decline by returning `null` for a given collection.
     *
     * @since 0.3.0
     */
    val modules: Set<(collection: DataCollection<*>) -> DataCollectionModule?> field = mutableSetOf<(collection: DataCollection<*>) -> DataCollectionModule?>()

    /**
     * The factories building the modules that run only where no ordinary module has answered, each of which may decline by returning `null` for a given collection.
     *
     * @since 0.3.0
     */
    val defaults: Set<(collection: DataCollection<*>) -> DataCollectionModule?> field = mutableSetOf<(collection: DataCollection<*>) -> DataCollectionModule?>()

    /**
     * Applies this description to the given registry entries.
     *
     * @param affect The registry entries this description applies to.
     * @param clear Whether to drop the entries already declared first. Defaults to `false`.
     * @return The current instance for further chaining.
     * @since 0.3.0
     */
    fun addAffects(vararg affect: ResourceKey<*>, clear: Boolean = false) = addAffects(clear) { it in affect }

    /**
     * Applies this description to every registry entry matching a predicate.
     *
     * @param clear Whether to drop the entries already declared first. Defaults to `false`.
     * @param affect A predicate deciding whether the description applies to a registry entry.
     * @return The current instance for further chaining.
     * @since 0.3.0
     */
    fun addAffects(clear: Boolean = false, affect: (key: ResourceKey<*>) -> Boolean): DataCollectionDescription {
        if (clear) clearAffects()
        affects.add(affect)
        return this
    }

    /**
     * Drops every registry entry this description applies to, leaving it applying to nothing.
     *
     * @return The current instance for further chaining.
     * @since 0.3.0
     */
    fun clearAffects(): DataCollectionDescription {
        affects.clear()
        return this
    }

    /**
     * Adds a module that always runs, built without regard to which collection it is being added to.
     *
     * @param module A factory building the module, which may return `null` to decline.
     * @param clear Whether to drop the modules already declared first. Defaults to `false`.
     * @return The current instance for further chaining.
     * @since 0.3.0
     */
    fun addModule(module: () -> DataCollectionModule?, clear: Boolean = false) = addModule(clear) { _ -> module() }

    /**
     * Adds a module that always runs, built against the collection it is being added to.
     *
     * @param clear Whether to drop the modules already declared first. Defaults to `false`.
     * @param module A factory building the module, given the collection, which may return `null` to decline.
     * @return The current instance for further chaining.
     * @since 0.3.0
     */
    fun addModule(clear: Boolean = false, module: (collection: DataCollection<*>) -> DataCollectionModule?): DataCollectionDescription {
        if (clear) clearModules()
        modules.add(module)
        return this
    }

    /**
     * Drops every module that always runs, leaving only the default modules.
     *
     * @return The current instance for further chaining.
     * @since 0.3.0
     */
    fun clearModules(): DataCollectionDescription {
        modules.clear()
        return this
    }

    /**
     * Adds a module that runs only where no ordinary module has answered, built without regard to which collection it is being added to.
     *
     * @param module A factory building the module, which may return `null` to decline.
     * @param clear Whether to drop the default modules already declared first. Defaults to `false`.
     * @return The current instance for further chaining.
     * @since 0.3.0
     */
    fun addDefaultModule(module: () -> DataCollectionModule?, clear: Boolean = false) = addDefaultModule(clear) { _ -> module() }

    /**
     * Adds a module that runs only where no ordinary module has answered, built against the collection it is being added to.
     *
     * @param clear Whether to drop the default modules already declared first. Defaults to `false`.
     * @param module A factory building the module, given the collection, which may return `null` to decline.
     * @return The current instance for further chaining.
     * @since 0.3.0
     */
    fun addDefaultModule(clear: Boolean = false, module: (collection: DataCollection<*>) -> DataCollectionModule?): DataCollectionDescription {
        if (clear) clearDefaultModules()
        defaults.add(module)
        return this
    }

    /**
     * Drops every default module, so that this description generates only what its ordinary modules generate.
     *
     * @return The current instance for further chaining.
     * @since 0.3.0
     */
    fun clearDefaultModules(): DataCollectionDescription {
        defaults.clear()
        return this
    }

}
