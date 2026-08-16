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

class EnergyStorageSpecialRenderer(val block: Block) : SpecialModelRenderer<Float> {

    val renderer = EnergyStorageRenderer()

    val default by lazy { block.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.UP) }

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

    data class Unbaked(val block: Block) : SpecialModelRenderer.Unbaked<Float> {

        override fun type() = codec

        override fun bake(context: SpecialModelRenderer.BakingContext) = EnergyStorageSpecialRenderer(block)

    }

    companion object {

        val codec = RecordCodecBuilder.mapCodec {
            it.group(
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base").forGetter(Unbaked::block)
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
