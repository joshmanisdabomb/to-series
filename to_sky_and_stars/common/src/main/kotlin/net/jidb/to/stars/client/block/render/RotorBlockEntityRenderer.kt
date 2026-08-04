package net.jidb.to.stars.client.block.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.jidb.to.base.mixin.client.BlockEntityRenderDispatcherAccessor
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.RotorBlock
import net.jidb.to.stars.block.entity.RotorBlockEntity
import net.jidb.to.stars.client.block.render.state.RotorBlockEntityState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.block.BlockModelRenderState
import net.minecraft.client.renderer.block.model.BlockDisplayContext
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING
import net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED
import net.minecraft.world.phys.Vec3

/**
 * Draws the rotor blades turning, which is only shown while they are actually being driven.
 *
 * The blades are drawn from the block's own model turned about its axis, rather than from a model of their own, and every other set is drawn from the alternate model so that a stack of them does not all line up.
 *
 * @param context What the renderer is built from.
 */
class RotorBlockEntityRenderer(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<RotorBlockEntity, RotorBlockEntityState> {

    /**
     * The blockstate the ordinary blades are drawn from.
     */
    val default by lazy { ToStarsMod.blocks.rotor_blades.defaultBlockState() }

    /**
     * The blockstate the alternate blades are drawn from.
     */
    val alt by lazy { ToStarsMod.blocks.rotor_blades.defaultBlockState().setValue(RotorBlock.alternate, true) }

    /**
     * The baked model of the ordinary blades.
     */
    val model by lazy { BlockModelRenderState().also { (Minecraft.getInstance().blockEntityRenderDispatcher as BlockEntityRenderDispatcherAccessor).`to_base$getBlockModelResolver`().update(
        it,
        default,
        BlockDisplayContext.create()
    ) } }

    /**
     * The baked model of the alternate blades.
     */
    val altModel by lazy { BlockModelRenderState().also { (Minecraft.getInstance().blockEntityRenderDispatcher as BlockEntityRenderDispatcherAccessor).`to_base$getBlockModelResolver`().update(
        it,
        alt,
        BlockDisplayContext.create()
    ) } }

    override fun submit(state: RotorBlockEntityState, stack: PoseStack, nodes: SubmitNodeCollector, camera: CameraRenderState) {
        if (!state.powered) return
        stack.pushPose()
        stack.translate(0.5, 0.5, 0.5)
        stack.mulPose(Axis.YP.rotationDegrees(180f - state.direction.toYRot()))
        stack.mulPose(Axis.ZP.rotationDegrees(state.angle.times(360f)))
        stack.translate(-0.5, -0.5, -0.5)
        (if (state.alternate) altModel else model).submit(stack, nodes, state.lightCoords, OverlayTexture.NO_OVERLAY, 0)
        stack.popPose()
    }

    override fun createRenderState() = RotorBlockEntityState()

    override fun extractRenderState(blockEntity: RotorBlockEntity, state: RotorBlockEntityState, partialTicks: Float, cameraPosition: Vec3, breakProgress: ModelFeatureRenderer.CrumblingOverlay?) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress)
        state.powered = blockEntity.blockState.getValue(POWERED)
        state.alternate = blockEntity.blockState.getValue(RotorBlock.alternate)
        state.direction = blockEntity.blockState.getValue(FACING)
        state.angle = blockEntity.clientPrevAngle + blockEntity.speed.times(partialTicks)
    }

}
