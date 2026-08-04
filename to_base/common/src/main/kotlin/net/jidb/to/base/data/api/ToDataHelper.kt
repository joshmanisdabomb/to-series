package net.jidb.to.base.data.api

import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.listDirectoryEntries

/**
 * A helper object for the data generators themselves, rather than for what they generate.
 *
 * @since 0.3.0
 */
object ToDataHelper {

    /**
     * Lists the generated directories of every namespace under a resource root, i.e. everything a generator is free to delete and rebuild.
     * This is what [net.jidb.to.base.data.pub.provider.DeleteDataProvider] clears out before a run, so that a file whose source has been removed does not linger.
     *
     * @param resources The resource root to list under, holding the `assets` and `data` directories.
     * @return The paths that a generator owns, whether or not each one currently exists.
     * @since 0.3.0
     */
    fun getReplaceableResources(resources: Path): List<Path> {
        val assets = resources / "assets"
        val data = resources / "data"
        return replaceableAssets.flatMap { asset -> assets.listDirectoryEntries().map { assets / it / asset } } + replaceableData.flatMap { asset -> data.listDirectoryEntries().map { data / it / asset } }
    }

    /**
     * The directories under a namespace's assets that a generator owns.
     *
     * @since 0.3.0
     */
    val replaceableAssets = listOf("blockstates", "items", "models", "particles", "lang", "wiki", "sounds.json")

    /**
     * The directories under a namespace's data that a generator owns.
     *
     * @since 0.3.0
     */
    val replaceableData = listOf("advancement", "loot_table", "recipe", "tags")

}
