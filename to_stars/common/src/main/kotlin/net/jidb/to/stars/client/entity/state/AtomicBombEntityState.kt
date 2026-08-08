package net.jidb.to.stars.client.entity.state

import net.minecraft.client.renderer.entity.state.EntityRenderState

/**
 * What is read off a falling atomic bomb in order to draw it.
 */
class AtomicBombEntityState : EntityRenderState() {

    /**
     * Which way the bomb faces.
     */
    var yRot: Float = 0f

    /**
     * How long the fuse has left, in ticks, or below zero where it is not burning.
     */
    var fuse: Float = 0f

}
