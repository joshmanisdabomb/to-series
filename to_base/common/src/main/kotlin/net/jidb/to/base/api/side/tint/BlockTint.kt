package net.jidb.to.base.api.side.tint

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockAndLightGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property

/**
 * A server-compatible recreation of the [net.minecraft.client.color.block.BlockTintSource] interface.
 * This can be loaded in [net.jidb.to.base.api.properties.ExtendedBlockProperties] and later registered by [net.jidb.to.base.client.api.properties.ExtendedClientBlockProperties].
 *
 * @since 0.7.0
 */
interface BlockTint {

    /**
     * Determines the color value associated with the given [BlockState].
     * Equivalent to [net.minecraft.client.color.block.BlockTintSource.color].
     *
     * @param state The [BlockState] for which the color is to be determined. May be null.
     * @return The computed color value as an integer.
     * @since 0.7.0
     */
    fun color(state: BlockState?): Int

    /**
     * Determines the color value associated with the given [BlockState] rendered in a [net.minecraft.world.level.Level] at a [BlockPos].
     * Equivalent to [net.minecraft.client.color.block.BlockTintSource.colorInWorld].
     *
     * @param state The [BlockState] for which the color is to be determined. May be null.
     * @param level The level the block state is located in. May be null.
     * @param pos The position the block state is located at. May be null.
     * @return The computed color value as an integer.
     * @since 0.7.0
     */
    fun color(state: BlockState?, level: BlockAndLightGetter?, pos: BlockPos?) = color(state)

    /**
     * Determines the color value for terrain particles associated with the given [BlockState].
     * Equivalent to [net.minecraft.client.color.block.BlockTintSource.colorAsTerrainParticle].
     *
     * @param state The [BlockState] for which the terrain particle color is to be determined. May be null.
     * @param level The level where the block state is located. May be null.
     * @param pos The position of the block state within the level. May be null.
     * @return The computed color value as an integer.
     * @since 0.7.0
     */
    fun colorTerrainParticles(state: BlockState?, level: BlockAndLightGetter?, pos: BlockPos?) = color(state)

    /**
     * Retrieves a set of properties that are relevant for the block tinting process.
     * Used to identify which properties of a block state impact the color calculations, equivalent to [net.minecraft.client.color.block.BlockTintSource.relevantProperties]
     *
     * @return A set of properties that are relevant for tinting computation.
     * @since 0.7.0
     */
    fun relevantProperties() = emptySet<Property<*>>()

}
