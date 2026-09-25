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

/**
 * Draws the item form of the rotor blades, turning at whatever speed they have been given.
 *
 * In the world the speed comes from how fast whoever is holding them is moving; in an inventory it comes from how fast the player is dragging them about, and either way it winds down of its own accord.
 *
 * @property gui Whether this is drawing the item in an interface rather than in the world.
 */
class RotorSpecialRenderer(val gui: Boolean) : SpecialModelRenderer<Float> {

    /**
     * When the last frame was drawn, which the turn since then is worked out against.
     */
    var frameTime: Long = 0L

    /**
     * The blockstate the item is drawn from.
     */
    val default by lazy { ToStarsMod.blocks.rotor_blades.defaultBlockState() }

    /**
     * The baked model of the blades.
     */
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

    /**
     * The declaration of this renderer as it is written in an item's model, before the models it needs have been baked.
     *
     * @property gui Whether this is drawing the item in an interface rather than in the world.
     */
    data class Unbaked(val gui: Boolean) : SpecialModelRenderer.Unbaked<Float> {

        override fun type() = codec

        override fun bake(context: SpecialModelRenderer.BakingContext) = RotorSpecialRenderer(gui)

    }

    companion object {

        /**
         * How fast each stack of blades is currently turning.
         */
        val rotationSpeeds = mutableMapOf<ItemStack, Float>()

        /**
         * How far round each stack of blades has turned.
         */
        val rotationAngles = mutableMapOf<ItemStack, Float>()

        /**
         * Where the mouse was, along x, when each stack was last drawn, which the drag is measured against.
         */
        val rotationMouseX = mutableMapOf<ItemStack, Double>()

        /**
         * Where the mouse was, along y, when each stack was last drawn.
         */
        val rotationMouseY = mutableMapOf<ItemStack, Double>()

        /**
         * The codec the declaration is read from an item's model through.
         */
        val codec = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.BOOL.fieldOf("gui").forGetter(Unbaked::gui)
            )
                .apply(it, ::Unbaked)
        }

        /**
         * The corners of the space the item is drawn within, which is the whole of one block.
         */
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
