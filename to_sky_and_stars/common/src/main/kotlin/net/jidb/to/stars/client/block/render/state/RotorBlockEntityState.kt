package net.jidb.to.stars.client.block.render.state

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.core.Direction

/**
 * What is read off a set of rotor blades in order to draw them.
 */
class RotorBlockEntityState : BlockEntityRenderState() {

    /**
     * How far round the blades have turned.
     */
    var angle: Float = 0f

    /**
     * Whether the blades are turning, which is when they are drawn at all.
     */
    var powered: Boolean = false

    /**
     * Whether the blades are drawn a half turn out from the set below them.
     */
    var alternate: Boolean = false

    /**
     * Which way the blades face.
     */
    var direction: Direction = Direction.NORTH

}
