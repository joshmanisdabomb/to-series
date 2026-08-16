package net.jidb.to.stars.client.block.render.state

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.core.Direction

class EnergyStorageBlockEntityState : BlockEntityRenderState() {

    var direction: Direction = Direction.UP

    var fill: Float = 0f

}
