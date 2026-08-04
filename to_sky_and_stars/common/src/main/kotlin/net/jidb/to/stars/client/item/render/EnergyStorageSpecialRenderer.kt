package net.jidb.to.stars.client.item.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.jidb.to.base.mixin.client.BlockEntityRenderDispatcherAccessor
import net.jidb.to.base.pub.transfer.energy.ToEnergyItemProvider
import net.jidb.to.stars.client.render.EnergyStorageRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.block.BlockModelRenderState
import net.minecraft.client.renderer.block.model.BlockDisplayContext
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import org.joml.Vector3f
import org.joml.Vector3fc
import java.util.function.Consumer

/**
 * Draws the item form of a block that stores To Energy, with its charge showing just as the block itself has it.
 *
 * @property block The block whose model the item is drawn from.
 */
class EnergyStorageSpecialRenderer(val block: Block) : SpecialModelRenderer<Float> {

    /**
     * The shared drawing of the charge, which the block itself uses too.
     */
    val renderer = EnergyStorageRenderer()

    /**
     * The blockstate the item is drawn from, faced up so that its charge shows on every side.
     */
    val default by lazy { block.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.UP) }

    /**
     * The baked model of the block.
     */
    val model by lazy { BlockModelRenderState()
        .also { (Minecraft.getInstance().blockEntityRenderDispatcher as BlockEntityRenderDispatcherAccessor).`to_base$getBlockModelResolver`().update(
            it,
            default,
            BlockDisplayContext.create()
        ) } }

    override fun submit(argument: Float?, stack: PoseStack, nodes: SubmitNodeCollector, lightCoords: Int, overlayCoords: Int, hasFoil: Boolean, outlineColor: Int) {
        stack.pushPose()
        model.submit(stack, nodes, lightCoords, overlayCoords, outlineColor)
        renderer.submit(argument ?: 0f, Direction.UP, stack, nodes)
        stack.popPose()
    }

    override fun getExtents(output: Consumer<Vector3fc>) {
        extents.forEach(output::accept)
    }

    override fun extractArgument(stack: ItemStack): Float {
        val energy = ToEnergyItemProvider.getTransferContext(stack) ?: return 0f
        return energy.getTotalAmount(Unit) / energy.getTotalCapacity(Unit).toFloat()
    }

    /**
     * The declaration of this renderer as it is written in an item's model, before the models it needs have been baked.
     *
     * @property block The block whose model the item is drawn from.
     */
    data class Unbaked(val block: Block) : SpecialModelRenderer.Unbaked<Float> {

        override fun type() = codec

        override fun bake(context: SpecialModelRenderer.BakingContext) = EnergyStorageSpecialRenderer(block)

    }

    companion object {

        /**
         * The codec the declaration is read from an item's model through.
         */
        val codec = RecordCodecBuilder.mapCodec {
            it.group(
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base").forGetter(Unbaked::block)
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
