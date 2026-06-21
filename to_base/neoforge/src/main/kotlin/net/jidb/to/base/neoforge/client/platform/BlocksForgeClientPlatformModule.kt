package net.jidb.to.base.neoforge.client.platform

import net.jidb.to.base.client.api.platform.BlocksClientPlatformModule
import net.jidb.to.base.neoforge.platform.DeferredForgeEventRegistry
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.client.event.EntityRenderersEvent

object BlocksForgeClientPlatformModule : BlocksClientPlatformModule() {

    val registry = DeferredForgeEventRegistry(EntityRenderersEvent.RegisterRenderers::class.java)

    override fun <B : BlockEntity, S : BlockEntityRenderState> registerBlockEntityRenderer(modid: String, type: () -> BlockEntityType<B>, renderer: BlockEntityRendererProvider<B, S>) {
        registry.register(modid) { event ->
            event.registerBlockEntityRenderer(type(), renderer)
        }
    }

}
