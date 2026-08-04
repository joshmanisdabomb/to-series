package net.jidb.to.base.client.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.color.block.BlockTintSource
import net.minecraft.world.level.block.Block

/**
 * [SimpleLibrary] implementation that registers a [BlockTintSource] against the blocks it colours, and provides access to those tint sources in one place.
 * A tint declared against a block through [net.jidb.to.base.api.properties.ExtendedBlockProperties] is handled by [net.jidb.to.base.client.api.properties.ExtendedClientBlockProperties] instead; this library is for a tint that is shared between blocks rather than declared by one.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.7.0
 */
open class BlockTintLibrary(modid: String) : SimpleLibrary<BlockTintSource>(modid) {

    /**
     * Declares a [BlockTintSource], registering it with the client platform against the given blocks when the library is built.
     *
     * @param S The type of the tint source being declared.
     * @param source The tint source that produces the colour.
     * @param blocks The blocks the tint source applies to.
     * @return The [Library.LibraryEntry] for the declared property.
     * @since 0.7.0
     */
    operator fun <S : BlockTintSource> invoke(source: S, vararg blocks: Block) = invoke({
        val source = it()
        ClientServices.platform.models.registerBlockTint(this.id.namespace, source, *blocks);
        { source }
    }, { source })

}
