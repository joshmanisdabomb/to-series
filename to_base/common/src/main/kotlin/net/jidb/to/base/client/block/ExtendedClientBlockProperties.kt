package net.jidb.to.base.client.block

import net.jidb.to.base.block.properties.ExtendedBlockProperties
import net.jidb.to.base.library.BlockLibrary
import net.minecraft.world.level.block.Block

object ExtendedClientBlockProperties {

    fun handle(properties: BlockLibrary.ExtendedBlockPropertiesList) = properties.list.forEach { (entry, properties) -> properties.forEach { handle(entry.value, it) } }

    fun handle(block: Block, properties: ExtendedBlockProperties) {

    }

}
