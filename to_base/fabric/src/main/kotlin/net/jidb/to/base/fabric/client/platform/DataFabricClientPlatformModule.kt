package net.jidb.to.base.fabric.client.platform

import net.jidb.to.base.client.api.platform.DataClientPlatformModule
import net.minecraft.client.data.models.model.TextureSlot

object DataFabricClientPlatformModule : DataClientPlatformModule() {

    override fun createTextureSlot(id: String) = TextureSlot.create(id)

}
