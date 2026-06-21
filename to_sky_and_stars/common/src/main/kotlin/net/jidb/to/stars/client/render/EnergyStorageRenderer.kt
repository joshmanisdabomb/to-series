package net.jidb.to.stars.client.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import net.jidb.to.stars.ToStarsMod
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.Direction
import net.minecraft.resources.Identifier
import net.minecraft.util.LightCoordsUtil

class EnergyStorageRenderer {

    fun submit(fill: Float, exclude: Direction? = null, stack: PoseStack, nodes: SubmitNodeCollector) {
        stack.pushPose()
        for (direction in Direction.Plane.HORIZONTAL) {
            if (direction == exclude) continue
            stack.pushPose()
            stack.translate(0.5, 0.5, 0.5)
            stack.mulPose(Axis.YP.rotationDegrees(180f - direction.toYRot()))
            stack.translate(-0.5, -0.5, -0.5)
            nodes.submitCustomGeometry(
                stack,
                RenderTypes.entitySolid(energy_storage_empty),
                getFillGeometry(0f, 1.0f - fill, direction, LightCoordsUtil.FULL_SKY)
            )
            nodes.submitCustomGeometry(
                stack,
                RenderTypes.entitySolid(energy_storage_full),
                getFillGeometry(1.0f - fill, 1f, direction, LightCoordsUtil.FULL_BRIGHT)
            )
            nodes.submitCustomGeometry(
                stack,
                RenderTypes.eyes(energy_storage_full),
                getFillGeometry(1.0f - fill, 1f, direction, LightCoordsUtil.FULL_BRIGHT)
            )
            stack.popPose()
        }
        stack.popPose()
    }

    private fun getFillGeometry(start: Float, end: Float, normal: Direction, light: Int): SubmitNodeCollector.CustomGeometryRenderer = { pose: PoseStack.Pose, buffer: VertexConsumer ->
        val top = 13 / 16f
        val height = 10 / 16f
        val startY = top - start * height
        val endY = top - end * height

        buffer.addVertex(pose, 9/16f, endY, 0.0f)
            .setColor(-1)
            .setUv(0f, end)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setNormal(pose, normal.step())
            .setLight(light)
        buffer.addVertex(pose, 7/16f, endY, 0.0f)
            .setColor(-1)
            .setUv(1f, end)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setNormal(pose, normal.step())
            .setLight(light)
        buffer.addVertex(pose, 7/16f, startY, 0.0f)
            .setColor(-1)
            .setUv(1f, start)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setNormal(pose, normal.step())
            .setLight(light)
        buffer.addVertex(pose, 9/16f, startY, 0.0f)
            .setColor(-1)
            .setUv(0f, start)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setNormal(pose, normal.step())
            .setLight(light)
    }

    companion object {
        val energy_storage_empty = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "textures/entity/block/power_bank/empty.png")
        val energy_storage_full = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "textures/entity/block/power_bank/full.png")
    }

}