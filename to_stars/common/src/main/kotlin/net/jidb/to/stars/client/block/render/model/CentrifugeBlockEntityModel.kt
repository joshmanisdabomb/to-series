package net.jidb.to.stars.client.block.render.model

import net.jidb.to.stars.client.block.render.state.CentrifugeBlockEntityState
import net.minecraft.client.model.Model
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.model.geom.builders.PartDefinition
import net.minecraft.client.renderer.rendertype.RenderTypes
import kotlin.math.pow

/**
 * The model of a centrifuge's moving parts, i.e. the drum that spins while it is working.
 *
 * @param root The baked parts of the model.
 */
class CentrifugeBlockEntityModel(root: ModelPart) : Model<CentrifugeBlockEntityState>(root, RenderTypes::entitySolid) {

    /**
     * The part the whole drum hangs off, which is what is turned.
     */
    val bone = root.getChild("bone")

    override fun setupAnim(state: CentrifugeBlockEntityState) {
        super.setupAnim(state)
        bone.yRot = state.progress.pow(1.15f) / 10f
    }

    companion object {

        /**
         * Builds the layer definition of the model.
         *
         * @return The layer definition.
         */
        fun create(): LayerDefinition {
            val mesh = MeshDefinition()
            val root = mesh.root

            val bone: PartDefinition =
                root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val hole1 = bone.addOrReplaceChild(
                "hole1", CubeListBuilder.create().texOffs(14, 20)
                    .addBox(-1.0f, -0.0941f, 1.3807f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 1.5708f, -0.2618f)
            )

            val hole2 = bone.addOrReplaceChild(
                "hole2", CubeListBuilder.create().texOffs(14, 20)
                    .addBox(-1.0f, -0.0941f, -3.3807f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 1.5708f, 0.2618f)
            )

            val hole3 = bone.addOrReplaceChild(
                "hole3", CubeListBuilder.create().texOffs(14, 20)
                    .addBox(-1.0f, -0.0941f, 1.3807f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.2618f, 0.0f, 0.0f)
            )

            val hole4 = bone.addOrReplaceChild(
                "hole4", CubeListBuilder.create().texOffs(14, 20)
                    .addBox(-1.0f, -0.0941f, -3.3807f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -0.2618f, 0.0f, 0.0f)
            )

            val plate = bone.addOrReplaceChild(
                "plate", CubeListBuilder.create().texOffs(14, 18)
                    .addBox(-3.0f, -1.0f, -5.0f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.0f))
                    .texOffs(22, 20).addBox(3.0f, -1.0f, 3.0f, 1.0f, 1.0f, 1.0f, CubeDeformation(0.0f))
                    .texOffs(22, 22).addBox(-4.0f, -1.0f, -4.0f, 1.0f, 1.0f, 1.0f, CubeDeformation(0.0f))
                    .texOffs(22, 22).addBox(3.0f, -1.0f, -4.0f, 1.0f, 1.0f, 1.0f, CubeDeformation(0.0f))
                    .texOffs(22, 20).addBox(-4.0f, -1.0f, 3.0f, 1.0f, 1.0f, 1.0f, CubeDeformation(0.0f))
                    .texOffs(0, 9).addBox(-4.0f, 0.0f, -3.0f, 8.0f, 1.0f, 6.0f, CubeDeformation(0.0f))
                    .texOffs(0, 0).addBox(-3.0f, 0.0f, -4.0f, 6.0f, 1.0f, 8.0f, CubeDeformation(0.0f))
                    .texOffs(0, 16).addBox(-5.0f, -1.0f, -3.0f, 1.0f, 1.0f, 6.0f, CubeDeformation(0.0f))
                    .texOffs(0, 16).addBox(4.0f, -1.0f, -3.0f, 1.0f, 1.0f, 6.0f, CubeDeformation(0.0f))
                    .texOffs(14, 16)
                    .addBox(-3.0f, -1.0f, 4.0f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
            )

            return LayerDefinition.create(mesh, 32, 32)
        }

    }

}
