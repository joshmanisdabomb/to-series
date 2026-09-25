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

/**
 * Draws the charge showing on the sides of something that stores To Energy, which both the block and its item are drawn with.
 *
 * The filled part is drawn twice, once solid and once as an overlay that ignores the light around it, which is what makes it glow.
 */
class EnergyStorageRenderer {

    /**
     * Draws the charge on every horizontal side but one.
     *
     * @param fill How full it is, from `0` to `1`.
     * @param exclude The side to leave alone, i.e. the one the block faces, or `null` to draw all four. Defaults to `null`.
     * @param stack The pose the drawing is done in.
     * @param nodes What the drawing is submitted to.
     */
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
                RenderTypes.entitySolid(energyStorageEmpty),
                getFillGeometry(0f, 1.0f - fill, direction, LightCoordsUtil.FULL_SKY)
            )
            nodes.submitCustomGeometry(
                stack,
                RenderTypes.entitySolid(energyStorageFull),
                getFillGeometry(1.0f - fill, 1f, direction, LightCoordsUtil.FULL_BRIGHT)
            )
            nodes.submitCustomGeometry(
                stack,
                RenderTypes.eyes(energyStorageFull),
                getFillGeometry(1.0f - fill, 1f, direction, LightCoordsUtil.FULL_BRIGHT)
            )
            stack.popPose()
        }
        stack.popPose()
    }

    /**
     * Builds the quad of one part of the charge bar, from one height up it to another.
     *
     * @param start Where the part begins, as a fraction up the bar.
     * @param end Where it ends.
     * @param normal Which way the quad faces.
     * @param light How brightly it is lit.
     * @return The geometry, ready to be submitted.
     */
    private fun getFillGeometry(start: Float, end: Float, normal: Direction, light: Int): SubmitNodeCollector.CustomGeometryRenderer = { pose: PoseStack.Pose, buffer: VertexConsumer ->
        val top = 13 / 16f
        val height = 10 / 16f
        val startY = top - start * height
        val endY = top - end * height

        buffer.addVertex(pose, 9 / 16f, endY, 0.0f)
            .setColor(-1)
            .setUv(0f, end)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setNormal(pose, normal.step())
            .setLight(light)
        buffer.addVertex(pose, 7 / 16f, endY, 0.0f)
            .setColor(-1)
            .setUv(1f, end)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setNormal(pose, normal.step())
            .setLight(light)
        buffer.addVertex(pose, 7 / 16f, startY, 0.0f)
            .setColor(-1)
            .setUv(1f, start)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setNormal(pose, normal.step())
            .setLight(light)
        buffer.addVertex(pose, 9 / 16f, startY, 0.0f)
            .setColor(-1)
            .setUv(0f, start)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setNormal(pose, normal.step())
            .setLight(light)
    }

    companion object {

        /**
         * The texture of the part of the bar that is not filled.
         */
        val energyStorageEmpty = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "textures/entity/block/power_bank/empty.png")

        /**
         * The texture of the part of the bar that is filled, which is also drawn as a glow.
         */
        val energyStorageFull = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "textures/entity/block/power_bank/full.png")

    }

}
