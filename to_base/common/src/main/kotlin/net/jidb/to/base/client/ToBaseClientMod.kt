package net.jidb.to.base.client

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.content.ToBaseClientContentMod
import net.jidb.to.base.client.pub.event.ToBaseClientEventLibrary
import net.jidb.to.base.client.pub.mod.ToClientMod
import net.jidb.to.base.client.pub.network.handler.ToBaseClientPayloadHandlerLibrary

/**
 * The client-side mod object for To Lay the Foundations. Extends [ToClientMod].
 *
 * @since 0.1.0
 */
object ToBaseClientMod : ToClientMod() {

    override val common get() = ToBaseMod

    /**
     * A sub mod object that holds the client-side "content" of To Lay the Foundations, such as the research desk.
     * This is here to keep the content of the mod separate from the API available to modders.
     *
     * @since 0.5.0
     */
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
