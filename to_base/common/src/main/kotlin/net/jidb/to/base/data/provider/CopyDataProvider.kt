package net.jidb.to.base.data.provider

import net.jidb.to.base.data.provider.DeleteDataProvider
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.copyToRecursively
import kotlin.io.path.deleteRecursively
import kotlin.io.path.div

open class CopyDataProvider(val output: PackOutput, val target: (output: Path) -> Path) : DataProvider {

    constructor(output: PackOutput, path: Path) : this(output, { path })

    @OptIn(ExperimentalPathApi::class)
    override fun run(cached: CachedOutput) = CompletableFuture.runAsync {
        val path = target(output.outputFolder)
        output.outputFolder.copyToRecursively(path, followLinks = false, overwrite = true)
        (path / ".cache").deleteRecursively()
    }

    override fun getName() = "Copy Data to Folder"

}