package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.BlockEntityRendererLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.block.render.CentrifugeBlockEntityRenderer
import net.jidb.to.stars.client.block.render.EnergyStorageBlockEntityRenderer
import net.jidb.to.stars.client.block.render.RotorBlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider

/**
 * [BlockEntityRendererLibrary] implementation holding how each of this mod's block entities is drawn.
 */
object ToStarsBlockEntityRenderersLibrary : BlockEntityRendererLibrary(ToStarsMod.modid) {

    /**
     * Draws the charge showing on the side of a power bank.
     */
    val power_bank by this(ToStarsMod.blockEntities::power_bank) { ctx: BlockEntityRendererProvider.Context -> EnergyStorageBlockEntityRenderer(ctx) }

    /**
     * Draws the rotor blades turning.
     */
    val rotor_blades by this(ToStarsMod.blockEntities::rotor_blades) { ctx: BlockEntityRendererProvider.Context -> RotorBlockEntityRenderer(ctx) }

    /**
     * Draws a centrifuge's drum spinning while it is processing.
     */
    val centrifuge by this(ToStarsMod.blockEntities::centrifuge) { ctx: BlockEntityRendererProvider.Context -> CentrifugeBlockEntityRenderer(ctx) }

}
