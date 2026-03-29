package net.jidb.to.base.fabric.client.platform

import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap
import net.jidb.to.base.client.platform.RenderClientPlatformModule
import net.minecraft.client.renderer.chunk.ChunkSectionLayer
import net.minecraft.world.level.block.Block

object RenderFabricClientPlatformModule : RenderClientPlatformModule() {

    override fun setRenderLayer(block: Block, layer: ChunkSectionLayer) = BlockRenderLayerMap.putBlock(block, layer)

}