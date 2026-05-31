package net.jidb.to.base.client.content

import net.jidb.to.base.client.mod.ToClientMod
import net.jidb.to.base.content.ToBaseContentMod

class ToBaseClientContentMod(override val common: ToBaseContentMod) : ToClientMod() {

    override val screens = ToBaseScreenLibrary
    override val reloadListeners = ToBaseClientReloadListenerLibrary

}
