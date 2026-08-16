package net.jidb.to.stars.client.item.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.jidb.to.base.mixin.client.BlockEntityRenderDispatcherAccessor
import net.jidb.to.stars.ToStarsMod
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.block.BlockModelRenderState
import net.minecraft.client.renderer.block.model.BlockDisplayContext
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.world.item.ItemStack
import org.joml.Vector3f
import org.joml.Vector3fc
import java.util.function.Consumer
import kotlin.math.abs

class RotorSpecialRenderer(val gui: Boolean) : SpecialModelRenderer<Float> {

    var frameTime: Long = 0L

    val default by lazy { ToStarsMod.blocks.rotor_blades.defaultBlockState() }

    val model by lazy { BlockModelRenderState()
        .also { (Minecraft.getInstance().blockEntityRenderDispatcher as BlockEntityRenderDispatcherAccessor).`to_base$getBlockModelResolver`().update(
            it,
            default,
            BlockDisplayContext.create()
        ) } }

    override fun submit(argument: Float?, stack: PoseStack, nodes: SubmitNodeCollector, lightCoords: Int, overlayCoords: Int, hasFoil: Boolean, outlineColor: Int) {
        stack.pushPose()
        stack.rotateAround(Axis.ZP.rotationDegrees((argument ?: 0f).times(360f)), 0.5f, 0.5f, 0.5f)
        model.submit(stack, nodes, lightCoords, overlayCoords, outlineColor)
        stack.popPose()
    }

    override fun getExtents(output: Consumer<Vector3fc>) {
        extents.forEach(output::accept)
    }

    override fun extractArgument(stack: ItemStack): Float {
        if (!Minecraft.getInstance().isPaused) {
            rotationSpeeds.replace(stack, (rotationSpeeds[stack] ?: 0f).times(0.99f))
        }
        val minecraft = Minecraft.getInstance()
        val frameTime = minecraft.frameTimeNs
        if (gui) {
            if (minecraft.player?.containerMenu?.carried === stack) {
                val mouseX = minecraft.mouseHandler.getScaledXPos(minecraft.window)
                val mouseY = minecraft.mouseHandler.getScaledYPos(minecraft.window)
                val seconds = frameTime / 1000000000f
                val deltaX = mouseX - (rotationMouseX[stack] ?: mouseX)
                val deltaY = mouseY - (rotationMouseY[stack] ?: mouseY)
                val rotationRate = if (!Minecraft.getInstance().isPaused) (rotationSpeeds[stack] ?: 0f) + abs(deltaX + deltaY).toFloat() * 4f * seconds else 0f
                val mouseRot = ((rotationAngles[stack] ?: 0f) + rotationRate * seconds) % 1f

                rotationMouseX[stack] = mouseX
                rotationMouseY[stack] = mouseY
                rotationSpeeds[stack] = rotationRate
                rotationAngles[stack] = mouseRot
                this.frameTime += frameTime

                return mouseRot
            }
        } else {
            val seconds = frameTime / 1000000000f
            val rotationRate = if (!Minecraft.getInstance().isPaused) rotationSpeeds[stack] ?: 0f else 0f
            val mouseRot = ((rotationAngles[stack] ?: 0f) + rotationRate * seconds) % 1f

            rotationAngles[stack] = mouseRot
            this.frameTime += frameTime

            return mouseRot
        }
        return 0f
    }

    data class Unbaked(val gui: Boolean) : SpecialModelRenderer.Unbaked<Float> {

        override fun type() = codec

        override fun bake(context: SpecialModelRenderer.BakingContext) = RotorSpecialRenderer(gui)

    }

    companion object {

        val rotationSpeeds = mutableMapOf<ItemStack, Float>()

        val rotationAngles = mutableMapOf<ItemStack, Float>()

        val rotationMouseX = mutableMapOf<ItemStack, Double>()

        val rotationMouseY = mutableMapOf<ItemStack, Double>()

        val codec = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.BOOL.fieldOf("gui").forGetter(Unbaked::gui)
            )
                .apply(it, ::Unbaked)
        }

        private val extents = arrayOf(
            Vector3f(0.0f, 0.0f, 0.0f),
            Vector3f(0.0f, 0.0f, 1.0f),
            Vector3f(0.0f, 1.0f, 0.0f),
            Vector3f(0.0f, 1.0f, 1.0f),
            Vector3f(1.0f, 0.0f, 0.0f),
            Vector3f(1.0f, 0.0f, 1.0f),
            Vector3f(1.0f, 1.0f, 0.0f),
            Vector3f(1.0f, 1.0f, 1.0f),
        )

    }

}
