package net.jidb.to.base.fabric.client.platform

import net.jidb.to.base.client.api.platform.DataClientPlatformModule
import net.minecraft.client.data.models.model.TextureSlot

/**
 * [DataClientPlatformModule] implementation for Fabric.
 *
 * @since 0.4.0
 */
object DataFabricClientPlatformModule : DataClientPlatformModule() {

    override fun createTextureSlot(id: String) = TextureSlot.create(id)

}
