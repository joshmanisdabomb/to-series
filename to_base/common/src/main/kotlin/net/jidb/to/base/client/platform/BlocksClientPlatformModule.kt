package net.jidb.to.base.client.platform

import net.minecraft.client.renderer.chunk.ChunkSectionLayer
import net.minecraft.world.level.block.Block

abstract class BlocksClientPlatformModule {

    abstract fun setRenderLayer(block: Block, layer: ChunkSectionLayer)

}