package net.jidb.to.base.client.api.properties

import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.api.properties.ExtendedBlockProperties
import net.jidb.to.base.client.service.ClientServices
import net.jidb.to.base.pub.library.BlockLibrary
import net.minecraft.client.color.block.BlockTintSource
import net.minecraft.client.renderer.block.BlockAndTintGetter
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

object ExtendedClientBlockProperties {

    fun handle(properties: BlockLibrary.ExtendedBlockPropertiesList) = properties.list.forEach { (entry, properties) -> properties.forEach { handle(entry.value, it) } }

    fun handle(block: Block, properties: ExtendedBlockProperties) {
        val tintProvider = properties.tint
        if (tintProvider != null) {
            val tint = tintProvider()
            ClientServices.platform.models.registerBlockTint(block.identifier.namespace, object : BlockTintSource {
                override fun color(state: BlockState) = tint.color(state)
                override fun colorInWorld(state: BlockState, level: BlockAndTintGetter, pos: BlockPos) = tint.color(state, level, pos)
                override fun colorAsTerrainParticle(state: BlockState, level: BlockAndTintGetter, pos: BlockPos) = tint.colorTerrainParticles(state, level, pos)
                override fun relevantProperties() = tint.relevantProperties()
            }, block)
        }
    }

}
