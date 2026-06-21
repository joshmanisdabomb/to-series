package net.jidb.to.base.data.api

import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.listDirectoryEntries

object ToDataHelper {

    fun getReplaceableResources(resources: Path): List<Path> {
        val assets = resources / "assets"
        val data = resources / "data"
        return replaceableAssets.flatMap { asset -> assets.listDirectoryEntries().map { assets / it / asset } } + replaceableData.flatMap { asset -> data.listDirectoryEntries().map { data / it / asset } }
    }

    val replaceableAssets = listOf("blockstates", "items", "models", "particles", "lang", "wiki", "sounds.json")
    val replaceableData = listOf("advancements", "loot_table", "recipe", "tags")

}
