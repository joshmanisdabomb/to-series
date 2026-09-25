package net.jidb.to.stars.client.block.render.state

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.core.Direction

/**
 * What is read off a power bank in order to draw it.
 */
class EnergyStorageBlockEntityState : BlockEntityRenderState() {

    /**
     * Which way the power bank faces, i.e. the one side its charge is not drawn on.
     */
    var direction: Direction = Direction.UP

    /**
     * How full the power bank is, from `0` to `1`.
     */
    var fill: Float = 0f

}
