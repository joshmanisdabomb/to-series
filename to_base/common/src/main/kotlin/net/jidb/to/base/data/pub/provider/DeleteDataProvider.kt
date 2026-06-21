package net.jidb.to.base.data.pub.provider

import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolute
import kotlin.io.path.deleteRecursively

open class DeleteDataProvider(val output: PackOutput, val targets: (output: Path) -> List<Path>) : DataProvider {

    constructor(output: PackOutput, path: List<Path>) : this(output, { path })
    constructor(output: PackOutput, path: Path) : this(output, listOf(path))

    @OptIn(ExperimentalPathApi::class)
    override fun run(cached: CachedOutput) = CompletableFuture.runAsync {
        val safe = System.getProperty("net.jidb.to.base.data.safe")
        if (safe == null) {
            System.err.println("System property net.jidb.to.base.data.safe not set, refusing to delete any files or folders.")
            return@runAsync
        }
        val safes = safe.split(";")

        val paths = targets(output.outputFolder)
        next@ for (target in paths) {
            val abs = target.absolute()
            if (safes.none { abs.startsWith(it) }) {
                System.err.println("Data provider tried to delete '$abs', but this is not defined in net.jidb.to.base.data.safe as a safe path to delete.")
                continue@next
            }

            abs.deleteRecursively()
            println("Deleted '$abs'.")
        }
    }

    override fun getName() = "Remove Data from Folder: ${targets(output.outputFolder).map(Path::getFileName).joinToString(", ")}"

}
