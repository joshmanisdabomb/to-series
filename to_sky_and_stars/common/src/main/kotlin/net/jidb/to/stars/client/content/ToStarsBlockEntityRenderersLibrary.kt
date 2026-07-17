package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.BlockEntityRendererLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.block.render.EnergyStorageBlockEntityRenderer
import net.jidb.to.stars.client.block.render.RotorBlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider

object ToStarsBlockEntityRenderersLibrary : BlockEntityRendererLibrary(ToStarsMod.modid) {

    val power_bank by this(ToStarsMod.blockEntities::power_bank) { ctx: BlockEntityRendererProvider.Context -> EnergyStorageBlockEntityRenderer(ctx) }
    val rotor_blades by this(ToStarsMod.blockEntities::rotor_blades) { ctx: BlockEntityRendererProvider.Context -> RotorBlockEntityRenderer(ctx) }

}
