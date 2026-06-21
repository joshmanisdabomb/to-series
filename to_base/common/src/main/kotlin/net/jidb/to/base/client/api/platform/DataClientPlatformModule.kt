package net.jidb.to.base.client.api.platform

import net.minecraft.client.data.models.model.TextureSlot

abstract class DataClientPlatformModule {

    abstract fun createTextureSlot(id: String): TextureSlot

}
