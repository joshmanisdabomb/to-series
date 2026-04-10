package net.jidb.to.base.client.data.model

import net.minecraft.world.level.block.Block

interface IExtendedBlockModelGenerators {

    fun createFullRotatedVariantBlock(block: Block): Unit
    fun createFire(block: Block): Unit

}
