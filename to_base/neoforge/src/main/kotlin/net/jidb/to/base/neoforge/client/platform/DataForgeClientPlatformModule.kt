package net.jidb.to.base.neoforge.client.platform

import net.jidb.to.base.client.platform.DataClientPlatformModule
import net.minecraft.client.data.models.model.TextureSlot

object DataForgeClientPlatformModule : DataClientPlatformModule() {

    override fun createTextureSlot(id: String) = TextureSlot.create(id)

}
