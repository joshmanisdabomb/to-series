package net.jidb.to.stars.client.block.render

import com.mojang.blaze3d.vertex.PoseStack
import net.jidb.to.stars.block.entity.EnergyStorageBlockEntity
import net.jidb.to.stars.client.block.render.state.EnergyStorageBlockEntityState
import net.jidb.to.stars.client.render.EnergyStorageRenderer
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.phys.Vec3

class EnergyStorageBlockEntityRenderer(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<EnergyStorageBlockEntity, EnergyStorageBlockEntityState> {

    val renderer = EnergyStorageRenderer()

    override fun submit(state: EnergyStorageBlockEntityState, stack: PoseStack, nodes: SubmitNodeCollector, camera: CameraRenderState) {
        renderer.submit(state.fill, state.direction, stack, nodes)
    }

    override fun createRenderState() = EnergyStorageBlockEntityState()

    override fun extractRenderState(blockEntity: EnergyStorageBlockEntity, state: EnergyStorageBlockEntityState, partialTicks: Float, cameraPosition: Vec3, breakProgress: ModelFeatureRenderer.CrumblingOverlay?) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress)
        state.fill = blockEntity.energy.energy.toFloat() / blockEntity.energy.capacity
        state.direction = blockEntity.blockState.getValue(DirectionalBlock.FACING)
    }

}
