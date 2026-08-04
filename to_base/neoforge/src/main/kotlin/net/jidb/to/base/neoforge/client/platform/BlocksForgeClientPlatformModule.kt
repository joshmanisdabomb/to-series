package net.jidb.to.base.neoforge.client.platform

import net.jidb.to.base.client.api.platform.BlocksClientPlatformModule
import net.jidb.to.base.neoforge.platform.DeferredForgeEventRegistry
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.client.event.EntityRenderersEvent

/**
 * [BlocksClientPlatformModule] implementation for Neoforge.
 *
 * A renderer cannot be registered whenever a mod asks, only while Neoforge is raising its own event, so every declaration is queued in a [DeferredForgeEventRegistry] and played back once that arrives.
 *
 * @since 0.4.0
 */
object BlocksForgeClientPlatformModule : BlocksClientPlatformModule() {

    /**
     * The queued registrations, played back when Neoforge raises the event they are waiting on.
     *
     * @since 0.4.0
     */
    val registry = DeferredForgeEventRegistry(EntityRenderersEvent.RegisterRenderers::class.java)

    override fun <B : BlockEntity, S : BlockEntityRenderState> registerBlockEntityRenderer(modid: String, type: () -> BlockEntityType<B>, renderer: BlockEntityRendererProvider<B, S>) {
        registry.register(modid) { event ->
            event.registerBlockEntityRenderer(type(), renderer)
        }
    }

}
