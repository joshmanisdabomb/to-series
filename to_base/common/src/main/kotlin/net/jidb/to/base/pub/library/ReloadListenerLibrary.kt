package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.api.platform.ReloadListenerPlatformModule
import net.minecraft.server.packs.resources.PreparableReloadListener

/**
 * [SimpleLibrary] implementation that registers [PreparableReloadListener] content with a platform module, and provides access to that content in one place.
 * The module is left to the subclass so that the same library serves both sides: a server listener registers against the data packs, and a client one against the resource packs.
 *
 * @param R The type of platform module the declared listeners are registered with.
 * @param modid The mod ID associated with the library.
 * @since 0.2.0
 */
abstract class ReloadListenerLibrary<R : ReloadListenerPlatformModule>(modid: String) : SimpleLibrary<PreparableReloadListener>(modid) {

    /**
     * The platform module the declared listeners are registered with, which subclasses supply for the side they cover.
     *
     * @since 0.2.0
     */
    abstract val registry: R

    override fun afterBuild(entry: Library<PreparableReloadListener, PreparableReloadListener>.LibraryEntry<out PreparableReloadListener, out PreparableReloadListener>) {
        registry.register(getEntryIdentifier(entry), entry.value)
    }

}
