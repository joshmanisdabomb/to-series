package net.jidb.to.stars.client.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.jidb.to.base.helper.KotlinHelper.squared
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.AtomicBombBlock
import net.jidb.to.stars.client.entity.state.AtomicBombEntityState
import net.jidb.to.stars.entity.AtomicBombEntity
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.TntMinecartRenderer
import net.minecraft.client.renderer.state.CameraRenderState
import net.minecraft.util.Mth

class AtomicBombEntityRenderer(context: EntityRendererProvider.Context) : EntityRenderer<AtomicBombEntity, AtomicBombEntityState>(context) {

    init {
        shadowRadius = 0.98f
    }

    override fun submit(state: AtomicBombEntityState, pose: PoseStack, nodes: SubmitNodeCollector, camera: CameraRenderState) {
        pose.pushPose()

        pose.translate(0.0f, 0.5f, 0.0f)
        if (state.fuse in 0.0..<10.0) {
            val swell = 1.0f + Mth.clamp(1.0f - state.fuse / 10.0f, 0.0f, 1.0f).squared().squared() * 0.3f
            pose.scale(swell, swell, swell)
        }
        pose.mulPose(Axis.YP.rotationDegrees(180f - state.yRot))
        pose.mulPose(Axis.YP.rotationDegrees(-90.0f))
        pose.translate(-0.5f, -0.5f, 0.5f)
        pose.mulPose(Axis.YP.rotationDegrees(90.0f))

        val flash = state.fuse >= 0 && state.fuse.toInt() % 20 < 8
        TntMinecartRenderer.submitWhiteSolidBlock(middle, pose, nodes, state.lightCoords, flash, state.outlineColor)
        pose.translate(-1f, 0f, 0f)
        TntMinecartRenderer.submitWhiteSolidBlock(head, pose, nodes, state.lightCoords, flash, state.outlineColor)
        pose.translate(2f, 0f, 0f)
        TntMinecartRenderer.submitWhiteSolidBlock(tail, pose, nodes, state.lightCoords, flash, state.outlineColor)
        pose.translate(-1f, 0f, 0f)

        pose.popPose()

        super.submit(state, pose, nodes, camera)
    }

    override fun createRenderState() = AtomicBombEntityState()

    override fun extractRenderState(entity: AtomicBombEntity, state: AtomicBombEntityState, partialTick: Float) {
        super.extractRenderState(entity, state, partialTick)
        state.yRot = entity.yRot
        state.fuse = entity.entityData[AtomicBombEntity.data_timer] - partialTick + 1.0f
    }

    companion object {
        val head by lazy { ToStarsMod.blocks.atomic_bomb.defaultBlockState().setValue(AtomicBombBlock.SEGMENT, AtomicBombBlock.AtomicBombSegment.HEAD) }
        val middle by lazy { ToStarsMod.blocks.atomic_bomb.defaultBlockState().setValue(AtomicBombBlock.SEGMENT, AtomicBombBlock.AtomicBombSegment.MIDDLE) }
        val tail by lazy { ToStarsMod.blocks.atomic_bomb.defaultBlockState().setValue(AtomicBombBlock.SEGMENT, AtomicBombBlock.AtomicBombSegment.TAIL) }
    }

}