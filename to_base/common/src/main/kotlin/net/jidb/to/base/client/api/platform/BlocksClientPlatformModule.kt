package net.jidb.to.base.client.api.platform

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

abstract class BlocksClientPlatformModule {

    abstract fun <B : BlockEntity, S : BlockEntityRenderState> registerBlockEntityRenderer(modid: String, type: () -> BlockEntityType<B>, renderer: BlockEntityRendererProvider<B, S>)

}