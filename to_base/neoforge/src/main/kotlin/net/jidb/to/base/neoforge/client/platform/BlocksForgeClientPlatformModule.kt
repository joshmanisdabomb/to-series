package net.jidb.to.base.neoforge.client.platform

import net.jidb.to.base.client.platform.BlocksClientPlatformModule
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.chunk.ChunkSectionLayer
import net.minecraft.world.level.block.Block

object BlocksForgeClientPlatformModule : BlocksClientPlatformModule() {

    override fun setRenderLayer(block: Block, layer: ChunkSectionLayer) = ItemBlockRenderTypes.setRenderLayer(block, layer)

}
