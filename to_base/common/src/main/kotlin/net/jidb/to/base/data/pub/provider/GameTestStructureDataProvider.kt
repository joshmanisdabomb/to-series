package net.jidb.to.base.data.pub.provider

import com.google.common.hash.Hashing
import net.jidb.to.base.api.gametest.ToGameTest
import net.jidb.to.base.pub.library.GameTestLibrary
import net.minecraft.SharedConstants
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtIo
import net.minecraft.nbt.NbtUtils
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import java.io.ByteArrayOutputStream
import java.util.concurrent.CompletableFuture
import kotlin.io.path.div

/**
 * A [DataProvider] that writes out the structure a game test is run in, for every test of a [GameTestLibrary] that did not name one built by hand.
 *
 * A game test cannot run without a structure file, and the ones these tests want are empty rooms with a floor, which is not worth building in game and exporting.
 * The file is written straight as NBT rather than through [net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate], since that reads a scene out of a live level and there is none on a generation run.
 * Every position is listed, air included, exactly as the structure block writes them.
 *
 * @property output The pack output the provider belongs to.
 * @property library The library whose tests are being generated for.
 * @since 1.0.0
 */
class GameTestStructureDataProvider(val output: PackOutput, val library: GameTestLibrary) : DataProvider {

    override fun run(cached: CachedOutput): CompletableFuture<*> {
        val root = output.getOutputFolder(PackOutput.Target.DATA_PACK)
        return CompletableFuture.allOf(*library.structures.map { (id, test) ->
            val structure = test.getStructure(id)
            val bytes = write(platform(test))
            CompletableFuture.runAsync {
                cached.writeIfNeeded(root / structure.namespace / DIRECTORY / "${structure.path}.nbt", bytes, Hashing.sha1().hashBytes(bytes))
            }
        }.toTypedArray())
    }

    /**
     * Builds the scene a test is run in: a box of air of the size it asked for, with its bottom layer filled in as its floor.
     *
     * @param test The test the scene is being built for.
     * @return The structure tag, ready to be written out.
     * @since 1.0.0
     */
    private fun platform(test: ToGameTest): CompoundTag {
        val air = Blocks.AIR.defaultBlockState()
        val floor = test.floor ?: air

        val palette = mutableListOf<BlockState>()
        val blocks = ListTag()
        for (x in 0 until test.size.x) {
            for (y in 0 until test.size.y) {
                for (z in 0 until test.size.z) {
                    val state = if (y == 0) floor else air
                    var index = palette.indexOf(state)
                    if (index < 0) {
                        index = palette.size
                        palette.add(state)
                    }
                    blocks.add(CompoundTag().apply {
                        put(POSITION, ints(x, y, z))
                        putInt(STATE, index)
                    })
                }
            }
        }

        return CompoundTag().apply {
            put(SIZE, ints(test.size.x, test.size.y, test.size.z))
            put(PALETTE, ListTag().apply { palette.forEach { add(NbtUtils.writeBlockState(it)) } })
            put(BLOCKS, blocks)
            put(ENTITIES, ListTag())
            putInt(SharedConstants.DATA_VERSION_TAG, SharedConstants.getCurrentVersion().dataVersion().version())
        }
    }

    /**
     * Wraps three coordinates as the list of integers a structure file records a position or a size as.
     *
     * @param x The first coordinate.
     * @param y The second coordinate.
     * @param z The third coordinate.
     * @return The list holding them.
     * @since 1.0.0
     */
    private fun ints(x: Int, y: Int, z: Int) = ListTag().apply {
        add(IntTag.valueOf(x))
        add(IntTag.valueOf(y))
        add(IntTag.valueOf(z))
    }

    /**
     * Compresses a structure tag into the bytes the file holds, which is what the cache hashes and writes.
     *
     * @param tag The structure tag being written.
     * @return The compressed bytes.
     * @since 1.0.0
     */
    private fun write(tag: CompoundTag): ByteArray = ByteArrayOutputStream().use {
        NbtIo.writeCompressed(tag, it)
        it.toByteArray()
    }

    override fun getName() = "Game Test Structures: ${library.modid}"

    companion object {

        /**
         * The directory of a data pack namespace that structure files live in.
         *
         * @since 1.0.0
         */
        private const val DIRECTORY = "structure"

        /**
         * The tag naming how large a structure is.
         *
         * @since 1.0.0
         */
        private const val SIZE = "size"

        /**
         * The tag holding the distinct block states a structure is built from.
         *
         * @since 1.0.0
         */
        private const val PALETTE = "palette"

        /**
         * The tag holding every position of a structure and which of its palette entries is there.
         *
         * @since 1.0.0
         */
        private const val BLOCKS = "blocks"

        /**
         * The tag holding the entities of a structure, which a generated one never has.
         *
         * @since 1.0.0
         */
        private const val ENTITIES = "entities"

        /**
         * The tag of a block naming where it is.
         *
         * @since 1.0.0
         */
        private const val POSITION = "pos"

        /**
         * The tag of a block naming which palette entry it is.
         *
         * @since 1.0.0
         */
        private const val STATE = "state"

    }

}
