package net.jidb.to.base.data.pub.provider

import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.concurrent.CompletableFuture
import kotlin.io.path.CopyActionResult
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.copyTo
import kotlin.io.path.copyToRecursively
import kotlin.io.path.createParentDirectories
import kotlin.io.path.deleteRecursively
import kotlin.io.path.div
import kotlin.io.path.isDirectory

/**
 * A [DataProvider] that copies a directory tree into the generated output rather than generating anything itself.
 * This is how content authored by hand, such as the wiki article sources, reaches the resources alongside what the generators build.
 *
 * The copy is recursive and overwrites what is already there, and the cache directory the generators leave behind is removed afterwards so it does not end up in the output.
 *
 * @property output The pack output the provider belongs to.
 * @property source The directory to copy from.
 * @property target The directory to copy into.
 * @property modify A function adjusting the destination of each file, given that destination and the file it came from. Defaults to leaving it as it is.
 * @since 0.0.4
 */
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
