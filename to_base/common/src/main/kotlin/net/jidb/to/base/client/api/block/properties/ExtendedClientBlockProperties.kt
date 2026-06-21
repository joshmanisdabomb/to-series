package net.jidb.to.base.client.api.block.properties

import net.jidb.to.base.api.block.properties.ExtendedBlockProperties
import net.jidb.to.base.pub.library.BlockLibrary
import net.minecraft.world.level.block.Block

object ExtendedClientBlockProperties {

    fun handle(properties: BlockLibrary.ExtendedBlockPropertiesList) = properties.list.forEach { (entry, properties) -> properties.forEach { handle(entry.value, it) } }

    fun handle(block: Block, properties: ExtendedBlockProperties) {

    }

}
