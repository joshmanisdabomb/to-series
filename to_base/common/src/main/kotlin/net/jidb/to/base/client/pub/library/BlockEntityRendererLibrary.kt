package net.jidb.to.base.client.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

open class BlockEntityRendererLibrary(modid: String) : SimpleLibrary<BlockEntityRendererProvider<out BlockEntity, out BlockEntityRenderState>>(modid) {

    operator fun <B : BlockEntity, S: BlockEntityRenderState> invoke(type: () -> BlockEntityType<B>, provider: (context: BlockEntityRendererProvider.Context) -> BlockEntityRenderer<B, S>): Library<BlockEntityRendererProvider<out BlockEntity, out BlockEntityRenderState>, BlockEntityRendererProvider<out BlockEntity, out BlockEntityRenderState>>.LibraryEntry<out BlockEntityRendererProvider<B, S>, out BlockEntityRendererProvider<B, S>> {
        return invoke({ provider ->
            ClientServices.platform.blocks.registerBlockEntityRenderer(modid, type, provider());
            provider
        }, { BlockEntityRendererProvider { provider(it) } })
    }

}
