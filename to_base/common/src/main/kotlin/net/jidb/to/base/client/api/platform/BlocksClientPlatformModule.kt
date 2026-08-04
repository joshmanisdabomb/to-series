package net.jidb.to.base.client.api.platform

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

/**
 * A [ClientPlatform]-specific module that defines a cross-platform contract for handling blocks and block entities.
 * This module has code for registering a [BlockEntityRendererProvider].
 *
 * @since 0.4.0
 */
abstract class BlocksClientPlatformModule {

    /**
     * Registers a [BlockEntityRendererProvider] for a specific [BlockEntityType].
     *
     * @param B The type of the block entity.
     * @param S The type of the block entity render state.
     * @param modid The unique namespace of the mod registering the renderer.
     * @param type A [BlockEntityType] supplier that the renderer is bound to.
     * @param renderer The provider responsible for creating the [net.minecraft.client.renderer.blockentity.BlockEntityRenderer] and its state.
     * @since 0.6.0
     */
    abstract fun <B : BlockEntity, S : BlockEntityRenderState> registerBlockEntityRenderer(modid: String, type: () -> BlockEntityType<B>, renderer: BlockEntityRendererProvider<B, S>)

}
