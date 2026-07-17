package net.jidb.to.stars.client.block.render.state

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.core.Direction

class RotorBlockEntityState : BlockEntityRenderState() {

    var angle: Float = 0f

    var powered: Boolean = false
    var alternate: Boolean = false
    var direction: Direction = Direction.NORTH

}