package net.jidb.to.base.fabric.client.platform

import net.jidb.to.base.client.api.platform.BlocksClientPlatformModule
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

object BlocksFabricClientPlatformModule : BlocksClientPlatformModule() {

    override fun <B : BlockEntity, S : BlockEntityRenderState> registerBlockEntityRenderer(modid: String, type: () -> BlockEntityType<B>, renderer: BlockEntityRendererProvider<B, S>) {
        BlockEntityRenderers.register(type(), renderer)
    }

}
