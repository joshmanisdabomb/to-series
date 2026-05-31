package net.jidb.to.base.client.gui

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