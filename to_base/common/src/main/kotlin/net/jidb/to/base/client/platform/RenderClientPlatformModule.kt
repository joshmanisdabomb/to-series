package net.jidb.to.base.client.platform

import net.minecraft.client.renderer.chunk.ChunkSectionLayer
import net.minecraft.world.level.block.Block

abstract class RenderClientPlatformModule {

    abstract fun setRenderLayer(block: Block, layer: ChunkSectionLayer)

}