package net.jidb.to.base.client.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.api.platform.ReloadListenerClientPlatformModule
import net.jidb.to.base.client.content.wiki.WikiArticleManager
import net.jidb.to.base.client.service.ClientServices
import net.jidb.to.base.pub.library.ReloadListenerLibrary

/**
 * [ReloadListenerLibrary] implementation that registers the client-side reload listeners of To Lay the Foundations, and provides access to them in one place.
 *
 * @since 0.2.0
 */
object ToBaseClientReloadListenerLibrary : ReloadListenerLibrary<ReloadListenerClientPlatformModule>(ToBaseMod.modid) {

    override val registry = ClientServices.platform.reloadListeners

    /**
     * Loads the wiki articles out of the resource packs whenever they are reloaded.
     *
     * @see WikiArticleManager
     * @since 0.2.0
     */
    val wiki_articles by this { WikiArticleManager }

}
