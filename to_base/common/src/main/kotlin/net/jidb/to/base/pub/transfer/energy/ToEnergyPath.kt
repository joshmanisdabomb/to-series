package net.jidb.to.base.pub.transfer.energy

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

/**
 * Implemented by a block that energy travels through rather than into, so that it can take a cut of what passes.
 * Every block along a route is asked in turn by [NetworkToEnergyTransferContext], so a cable that loses energy over distance, or one that caps how much it will carry, only has to answer this.
 *
 * @since 0.6.0
 */
interface ToEnergyPath {

    /**
     * Adjusts the amount of energy passing through this block on its way along a route.
     * The default keeps the amount as it is, i.e. the block is a perfect conductor.
     *
     * @param amount The amount of energy that reached this block.
     * @param level The level the block is in.
     * @param state The state of the block being passed through.
     * @param pos The position of the block being passed through.
     * @param extract Whether the energy is being pulled along the route rather than pushed along it.
     * @return The amount of energy that carries on past this block.
     * @since 0.6.0
     */
    fun changeEnergy(amount: Long, level: Level, state: BlockState, pos: BlockPos, extract: Boolean) = amount

}
