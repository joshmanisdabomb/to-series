package net.jidb.to.base.api.properties

import net.jidb.to.base.api.side.tint.BlockTint
import net.jidb.to.base.mixin.FireBlockAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FireBlock

/**
 * A class that acts as an extension of [net.minecraft.world.level.block.state.BlockBehaviour.Properties] that allows changing of properties for a [Block] that aren't easily changed in vanilla code.
 * Currently has support for [FireBlock] flammability, and block tinting.
 *
 * [build] needs to be called so any changes are actually registered.
 * Some properties that exist here, such as [tint], will need to be built client-side instead. They should be registered here and built with [net.jidb.to.base.client.api.properties.ExtendedClientBlockProperties].
 *
 * @since 0.1.0
 */
class ExtendedBlockProperties {

    /**
     * A map of this [Block]'s ignite odds associated with the [FireBlock] (typically [Blocks.FIRE]) that can catch it alight.
     *
     * @see flammable
     * @see FireBlockAccessor.`to_base$getIgniteOdds`
     * @since 0.1.0
     */
    val fireIgnite: Map<FireBlock, Int> field = mutableMapOf<FireBlock, Int>()

    /**
     * A map of this [Block]'s burn odds associated with the [FireBlock] (typically [Blocks.FIRE]) that can remove the block.
     *
     * @see flammable
     * @see FireBlockAccessor.`to_base$getBurnOdds``
     * @since 0.1.0
     */
    val fireBurn: Map<FireBlock, Int> field = mutableMapOf<FireBlock, Int>()

    /**
     * Defines a tint provider for the [Block] that determines how the [Block] is colored.
     * [BlockTint] can exist server-side, but will be registered for this [Block] client-side.
     *
     * @see BlockTint
     * @see ExtendedBlockProperties.setTint
     * @since 0.6.0
     */
    var tint: (() -> BlockTint)? = null
        private set

    /**
     * Sets the flammability properties for this [Block], specifying how quickly it catches fire and how fast it burns.
     * See [Flammable Blocks](https://minecraft.wiki/w/Fire#Flammable_blocks) for more information on the odds.
     *
     * @param igniteOdds The likelihood of the block catching fire from a neighboring source. Defaults to 5.
     * @param burnOdds The likelihood of the block being consumed by fire once alight. Defaults to 20.
     * @param block The fire block instance (typically [Blocks.FIRE]) used to associate the flammability properties.
     * @return The current instance of [ExtendedBlockProperties] for further chaining.
     * @since 0.1.0
     */
    fun flammable(igniteOdds: Int = 5, burnOdds: Int = 20, block: FireBlock = Blocks.FIRE as FireBlock): ExtendedBlockProperties {
        fireIgnite[block] = igniteOdds
        fireBurn[block] = burnOdds
        return this
    }

    /**
     * Sets the tinting function for this [Block], determining how the color is calculated.
     * The tint function is responsible for returning the desired tint color for the block based on its state, position, or other context.
     *
     * @param tint A [BlockTint] interface that contains a server-compatible method for tinting the block based on [net.minecraft.world.level.block.state.BlockState], and optional [net.minecraft.world.level.Level] and [net.minecraft.core.BlockPos].
     * @return The current instance of [ExtendedBlockProperties] to allow method chaining.
     * @since 0.6.0
     */
    fun setTint(tint: () -> BlockTint): ExtendedBlockProperties {
        this.tint = tint
        return this
    }

    /**
     * Registers the server-side values set with the builder functions, to actually make the changes in-game.
     * Some properties in [ExtendedBlockProperties], such as [tint], will need to be built client-side instead. They should be built with [net.jidb.to.base.client.api.properties.ExtendedClientBlockProperties.handle].
     *
     * @param block The [Block] associated with this [ExtendedBlockProperties] to commit the properties to.
     * @return The current instance of [ExtendedBlockProperties] to allow method chaining.
     * @since 0.1.0
     */
    fun build(block: Block): ExtendedBlockProperties {
        for ((fire, ignite) in fireIgnite) {
            (fire as FireBlockAccessor).`to_base$setFlammable`(block, ignite, fireBurn[fire]!!)
        }
        return this
    }

}
