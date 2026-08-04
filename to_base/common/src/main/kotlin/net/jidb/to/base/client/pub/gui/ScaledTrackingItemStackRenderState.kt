package net.jidb.to.base.client.pub.gui

import com.mojang.blaze3d.vertex.PoseStack
import net.jidb.to.base.mixin.client.GuiGraphicsAccessor
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.item.TrackingItemStackRenderState
import net.minecraft.client.renderer.state.gui.GuiItemRenderState
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import org.joml.Matrix3x2f
import org.joml.Vector3f
import org.joml.Vector3fc
import java.util.function.Consumer

/**
 * A [TrackingItemStackRenderState] that draws its item at a scale other than the one the GUI would normally use.
 * Vanilla has no way to ask for a larger or smaller item in a GUI, so the scale is applied to the pose the item is submitted with, and to the extents and bounding box that decide how much room it is given.
 *
 * @property scale The factor the item is drawn at, where `1` is the size vanilla would draw it.
 * @since 0.1.0
 */
class ScaledTrackingItemStackRenderState(val scale: Float) : TrackingItemStackRenderState() {

    override fun visitExtents(visitor: Consumer<Vector3fc>) = super.visitExtents { v -> visitor.accept(Vector3f(v).mul(scale)) }

    override fun submit(poseStack: PoseStack, nodeCollector: SubmitNodeCollector, packedLight: Int, packedOverlay: Int, outlineColor: Int) {
        poseStack.pushPose()
        poseStack.scale(scale, scale, scale)
        super.submit(poseStack, nodeCollector, packedLight, packedOverlay, outlineColor)
        poseStack.popPose()
    }

    override fun getModelBoundingBox() = super.getModelBoundingBox().inflate(0.25)

    companion object {

        /**
         * Draws an item stack into a GUI at the given scale, in the same way [net.minecraft.client.gui.GuiGraphics] draws one at its normal size.
         * The item is kept centred on the position it would have occupied unscaled, which is what the offset accounts for.
         *
         * @param graphics The graphics to draw the item into.
         * @param stack The item stack to draw.
         * @param x The x position to draw the item at.
         * @param y The y position to draw the item at.
         * @param scale The factor to draw the item at, where `1` is the size vanilla would draw it. Defaults to `1`.
         * @param scissors The rectangle to clip the item to, or `null` for no clipping. Defaults to `null`.
         * @param seed The seed used to pick between the random model variants of the item. Defaults to `0`.
         * @since 0.6.0
         */
        fun extractItem(graphics: GuiGraphicsExtractor, stack: ItemStack, x: Int, y: Int, scale: Float = 1f, scissors: ScreenRectangle? = null, seed: Int = 0) {
            val minecraft = Minecraft.getInstance()
            val state = ScaledTrackingItemStackRenderState(scale)

            minecraft.itemModelResolver.updateForTopItem(state, stack, ItemDisplayContext.GUI, minecraft.level, minecraft.player, seed)
            state.isOversizedInGui = true

            val offset = ((scale - 1f) * 8f).toInt()

            val renderState = GuiItemRenderState(
                Matrix3x2f(graphics.pose()),
                state,
                x + offset,
                y + offset,
                scissors
            )

            (graphics as GuiGraphicsAccessor).`to_base$getGuiRenderState`().addItem(renderState)
        }

    }

}
