package net.jidb.to.base.client.data.api.model

import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.world.level.block.Block

interface IExtendedBlockModelGenerators {

    fun createFullRotatedVariantBlock(block: Block, model: TexturedModel.Provider = TexturedModel.CUBE): Unit
    fun createUprightDirectionalBlock(block: Block, top: TexturedModel.Provider, side: TexturedModel.Provider, bottom: TexturedModel.Provider): Unit

    fun createFire(block: Block): Unit

}
