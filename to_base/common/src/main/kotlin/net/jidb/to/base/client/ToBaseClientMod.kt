package net.jidb.to.base.client

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.content.ToBaseClientContentMod
import net.jidb.to.base.client.mod.ToClientMod
import net.jidb.to.base.client.network.handler.ToBaseClientPayloadHandlerLibrary

object ToBaseClientMod : ToClientMod() {
    override val common get() = ToBaseMod

    val content by lazy { ToBaseClientContentMod(common.content) }

    override val payloadHandlers = ToBaseClientPayloadHandlerLibrary

    override fun clientInit() {
        super.clientInit()
        content.clientInit()
    }

    override fun clientSetup() {
        super.clientSetup()
        content.clientSetup()
    }
}
