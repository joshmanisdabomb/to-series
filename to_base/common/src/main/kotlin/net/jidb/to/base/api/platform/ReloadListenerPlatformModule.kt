package net.jidb.to.base.api.platform

import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.PreparableReloadListener

abstract class ReloadListenerPlatformModule {

    abstract fun register(identifier: Identifier, listener: PreparableReloadListener)

}
