package net.jidb.to.base.client.api.platform

import net.minecraft.client.data.models.model.TextureSlot

/**
 * A [ClientPlatform]-specific module that defines a cross-platform contract for the client-side data generators.
 * This module has code for creating a [TextureSlot].
 *
 * @since 0.4.0
 */
abstract class DataClientPlatformModule {

    /**
     * Creates a new [TextureSlot] under the given name, for a model template that needs a texture slot vanilla does not already define.
     *
     * The constructor is inaccessible in vanilla code, but modloaders make it accessible separately.
     *
     * @param id The name of the texture slot, as it is written in the model JSON.
     * @return A new [TextureSlot] with the given name.
     * @since 0.4.0
     */
    abstract fun createTextureSlot(id: String): TextureSlot

}
