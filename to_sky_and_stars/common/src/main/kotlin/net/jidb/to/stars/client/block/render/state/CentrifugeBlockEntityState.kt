package net.jidb.to.stars.client.block.render.state

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState

/**
 * What is read off a centrifuge in order to draw it, so that the drawing itself need not touch the block entity.
 */
class CentrifugeBlockEntityState : BlockEntityRenderState() {

    /**
     * How far through its recipe the centrifuge is, which is what the drum's angle follows from.
     */
    var progress: Float = 0f

    /**
     * Whether the centrifuge is working, which is when the drum is drawn at all.
     */
    var lit: Boolean = false

}
