package net.jidb.to.stars.client.block.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.CentrifugeBlockEntity
import net.jidb.to.stars.client.block.render.model.CentrifugeBlockEntityModel
import net.jidb.to.stars.client.block.render.state.CentrifugeBlockEntityState
import net.jidb.to.stars.client.content.ToStarsModelLayersLibrary
import net.minecraft.client.renderer.Sheets
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.Vec3

class CentrifugeBlockEntityRenderer(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<CentrifugeBlockEntity, CentrifugeBlockEntityState> {

    val model = CentrifugeBlockEntityModel(context.bakeLayer(ToStarsModelLayersLibrary.centrifuge))
    val sprites = context.sprites()

    override fun submit(state: CentrifugeBlockEntityState, stack: PoseStack, nodes: SubmitNodeCollector, camera: CameraRenderState) {
        if (!state.lit) return
        stack.pushPose()
        stack.translate(0.5, 0.5, 0.5)
        stack.mulPose(Axis.XP.rotationDegrees(180f))
        this.model.setupAnim(state)
        nodes.submitModel(this.model, state, stack, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, texture, sprites, 0, state.breakProgress)
        stack.popPose()
    }

    override fun createRenderState() = CentrifugeBlockEntityState()

    override fun extractRenderState(blockEntity: CentrifugeBlockEntity, state: CentrifugeBlockEntityState, partialTicks: Float, cameraPosition: Vec3, breakProgress: ModelFeatureRenderer.CrumblingOverlay?) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress)
        state.lit = blockEntity.blockState.getValue(BlockStateProperties.LIT)
        state.progress = blockEntity.progress + partialTicks
    }

    companion object {
        val texture = Sheets.BLOCK_ENTITIES_MAPPER.apply(Identifier.fromNamespaceAndPath(ToStarsMod.modid, "block/centrifuge"))
    }

}
