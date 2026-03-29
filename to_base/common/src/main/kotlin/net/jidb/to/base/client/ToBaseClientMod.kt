package net.jidb.to.base.client

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.content.ToBaseClientReloadListenerLibrary
import net.jidb.to.base.client.content.ToBaseScreenLibrary
import net.jidb.to.base.client.mod.ToClientMod

object ToBaseClientMod : ToClientMod() {
    override val common get() = ToBaseMod

    override val screens = ToBaseScreenLibrary
    override val reloadListeners = ToBaseClientReloadListenerLibrary
}
