package net.jidb.to.stars.client.block.render.state

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState

class CentrifugeBlockEntityState : BlockEntityRenderState() {

    var progress: Float = 0f
    var lit: Boolean = false

}