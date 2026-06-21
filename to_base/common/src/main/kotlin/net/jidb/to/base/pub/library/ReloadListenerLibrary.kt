package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.api.platform.ReloadListenerPlatformModule
import net.minecraft.server.packs.resources.PreparableReloadListener

abstract class ReloadListenerLibrary<R : ReloadListenerPlatformModule>(modid: String) : SimpleLibrary<PreparableReloadListener>(modid) {

    abstract val registry: R

    override fun afterBuild(entry: Library<PreparableReloadListener, PreparableReloadListener>.LibraryEntry<out PreparableReloadListener, out PreparableReloadListener>) {
        registry.register(getEntryIdentifier(entry), entry.value)
    }

}
