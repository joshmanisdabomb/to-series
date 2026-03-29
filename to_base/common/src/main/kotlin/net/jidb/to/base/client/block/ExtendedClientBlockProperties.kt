package net.jidb.to.base.client.block

import net.jidb.to.base.block.properties.ExtendedBlockProperties
import net.jidb.to.base.client.service.ClientServices
import net.jidb.to.base.library.BlockLibrary
import net.minecraft.client.renderer.chunk.ChunkSectionLayer
import net.minecraft.world.level.block.Block

object ExtendedClientBlockProperties {

    fun handle(properties: BlockLibrary.ExtendedBlockPropertiesList) = properties.list.forEach { (entry, properties) -> properties.forEach { handle(entry.value, it) } }

    fun handle(block: Block, properties: ExtendedBlockProperties) {
        ClientServices.platform.rendering.setRenderLayer(block, when (properties.renderLayer) {
            ExtendedBlockProperties.RenderLayer.CUTOUT -> ChunkSectionLayer.CUTOUT
            ExtendedBlockProperties.RenderLayer.TRANSLUCENT -> ChunkSectionLayer.TRANSLUCENT
            ExtendedBlockProperties.RenderLayer.TRIPWIRE -> ChunkSectionLayer.TRIPWIRE
            else -> ChunkSectionLayer.SOLID
        })
    }

}
