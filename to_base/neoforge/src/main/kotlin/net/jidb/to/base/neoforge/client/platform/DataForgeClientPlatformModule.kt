package net.jidb.to.base.neoforge.client.platform

import net.jidb.to.base.client.api.platform.DataClientPlatformModule
import net.minecraft.client.data.models.model.TextureSlot

/**
 * [DataClientPlatformModule] implementation for Neoforge.
 *
 * @since 0.4.0
 */
object DataForgeClientPlatformModule : DataClientPlatformModule() {

    override fun createTextureSlot(id: String) = TextureSlot.create(id)

}
