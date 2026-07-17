package net.jidb.to.base.client

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.content.ToBaseClientContentMod
import net.jidb.to.base.client.pub.event.ToBaseClientEventLibrary
import net.jidb.to.base.client.pub.mod.ToClientMod
import net.jidb.to.base.client.pub.network.handler.ToBaseClientPayloadHandlerLibrary

object ToBaseClientMod : ToClientMod() {
    override val common get() = ToBaseMod

    val content by lazy { ToBaseClientContentMod(common.content) }

    override val events = ToBaseClientEventLibrary
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
