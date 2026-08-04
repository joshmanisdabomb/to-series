package net.jidb.to.base.client.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

/**
 * [SimpleLibrary] implementation that registers a [BlockEntityRenderer] against the [BlockEntityType] it draws, and provides access to those renderers in one place.
 * The block entity types themselves live in a common library, and are taken here as suppliers so that a renderer can be declared without the type having been built yet.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.6.0
 */
open class BlockEntityRendererLibrary(modid: String) : SimpleLibrary<BlockEntityRendererProvider<out BlockEntity, out BlockEntityRenderState>>(modid) {

    /**
     * Declares a [BlockEntityRenderer] for the given block entity type, registering it with the client platform when the library is built.
     *
     * @param B The type of the block entity being rendered.
     * @param S The type of the render state the renderer builds.
     * @param type A [BlockEntityType] supplier that the renderer is bound to.
     * @param provider A function that builds the renderer, given the context the client hands renderers at load time.
     * @return The [Library.LibraryEntry] for the declared property.
     * @since 0.6.0
     */
    operator fun <B : BlockEntity, S : BlockEntityRenderState> invoke(type: () -> BlockEntityType<B>, provider: (context: BlockEntityRendererProvider.Context) -> BlockEntityRenderer<B, S>) = invoke({ provider ->
        ClientServices.platform.blocks.registerBlockEntityRenderer(modid, type, provider())
        provider
    }, { BlockEntityRendererProvider { provider(it) } })

}
