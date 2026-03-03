package net.jidb.to.base.data.provider

import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolute
import kotlin.io.path.copyToRecursively
import kotlin.io.path.deleteRecursively

class DeleteDataProvider(val output: PackOutput, val paths: (output: Path) -> List<Path>) : DataProvider {

    @OptIn(ExperimentalPathApi::class)
    override fun run(cached: CachedOutput) = CompletableFuture.runAsync {
        val safe = System.getProperty("net.jidb.to.base.data.safe")
        if (safe == null) {
            System.err.println("System property net.jidb.to.base.data.safe not set, refusing to delete any files or folders.")
            return@runAsync
        }
        val safes = safe.split(";")

        val targets = paths(output.outputFolder)
        next@ for (target in targets) {
            val abs = target.absolute()
            if (safes.none { abs.startsWith(it) }) {
                System.err.println("Data provider tried to delete '$abs', but this is not defined in net.jidb.to.base.data.safe as a safe path to delete.")
                continue@next
            }

            abs.deleteRecursively()
            println("Deleted '$abs'.")
        }
    }

    override fun getName() = "Remove Data from Folder"

}
