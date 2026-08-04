package net.jidb.to.base.pub.transfer.energy

import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.LevelReader

/**
 * Implemented by anything that can say how much To Energy a block in the world holds, i.e. the block entity there or the block itself.
 * The side is passed through, so a machine can offer different energy to each of its faces, as [InputToEnergyTransferContext] and [OutputToEnergyTransferContext] do.
 *
 * @see ToEnergyItemProvider
 * @since 0.6.0
 */
interface ToEnergyWorldlyProvider {

    /**
     * Retrieves the energy held by the block at the given position.
     *
     * @param level The level the block is in.
     * @param pos The position of the block to read the energy of.
     * @param side The face the energy is being reached through, or `null` where it is being read from no particular side.
     * @return The [ToEnergyTransferContext] for that position and side, or `null` where this provider holds no energy for it.
     * @since 0.6.0
     */
    fun getTransferContext(level: LevelReader, pos: BlockPos, side: Direction?): ToEnergyTransferContext?

    companion object {

        /**
         * Finds the provider for a position, preferring the block entity there over the block itself.
         *
         * @param level The level the block is in.
         * @param pos The position to find a provider for.
         * @return The first provider found, or `null` where neither the block entity nor the block is one.
         * @since 0.6.0
         */
        fun getProvider(level: LevelReader, pos: BlockPos): ToEnergyWorldlyProvider? {
            val entity = level.getBlockEntity(pos)
            if (entity is ToEnergyWorldlyProvider) {
                return entity
            }
            val block = level.getBlockState(pos).block
            if (block is ToEnergyWorldlyProvider) {
                return block
            }
            return null
        }

        /**
         * Retrieves the energy held by a block in the world, by finding its provider with [getProvider] and asking that.
         *
         * @param level The level the block is in.
         * @param pos The position of the block to read the energy of.
         * @param side The face the energy is being reached through, or `null` where it is being read from no particular side.
         * @return The [ToEnergyTransferContext] for that position and side, or `null` where the block holds no energy.
         * @since 0.6.0
         */
        fun getTransferContext(level: LevelReader, pos: BlockPos, side: Direction?) = getProvider(level, pos)?.getTransferContext(level, pos, side)

    }

}
