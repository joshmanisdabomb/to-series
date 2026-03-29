package net.jidb.to.base.client.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.platform.ReloadListenerClientPlatformModule
import net.jidb.to.base.client.service.ClientServices
import net.jidb.to.base.client.wiki.WikiArticleManager
import net.jidb.to.base.library.ReloadListenerLibrary

object ToBaseClientReloadListenerLibrary : ReloadListenerLibrary<ReloadListenerClientPlatformModule>(ToBaseMod.modid) {

    override val registry = ClientServices.platform.reloadListeners

    val wiki_articles by this { WikiArticleManager }

}
