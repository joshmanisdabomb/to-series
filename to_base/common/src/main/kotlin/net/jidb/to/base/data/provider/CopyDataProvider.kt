package net.jidb.to.base.data.provider

import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.concurrent.CompletableFuture
import kotlin.io.path.*

open class CopyDataProvider(val output: PackOutput, val source: Path, val target: Path, val modify: (destination: Path, source: Path) -> Path = { dst, _ -> dst }) : DataProvider {

    @OptIn(ExperimentalPathApi::class)
    override fun run(cached: CachedOutput) = CompletableFuture.runAsync {
        source.copyToRecursively(target, followLinks = false) { src, dst ->
            val dst = modify(dst, src)

            //Original Kotlin code
            val dstIsDirectory = dst.isDirectory(LinkOption.NOFOLLOW_LINKS)
            val srcIsDirectory = src.isDirectory(LinkOption.NOFOLLOW_LINKS)
            if ((srcIsDirectory && dstIsDirectory).not()) {
                if (dstIsDirectory) dst.deleteRecursively()
                src.copyTo(dst.createParentDirectories(), LinkOption.NOFOLLOW_LINKS, StandardCopyOption.REPLACE_EXISTING)
            }

            CopyActionResult.CONTINUE
        }
        (target / ".cache").deleteRecursively()
    }

    override fun getName() = "Copy Data to Folder: ${source.fileName} to ${target.fileName}"

}