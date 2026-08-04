package net.jidb.to.base.client.content

import net.jidb.to.base.client.pub.mod.ToClientMod
import net.jidb.to.base.content.ToBaseContentMod

/**
 * The client-side mod object holding the game content of To Lay the Foundations, the client counterpart of [ToBaseContentMod].
 *
 * @property common The common-side content mod this one draws its content from.
 * @since 0.5.0
 */
class ToBaseClientContentMod(override val common: ToBaseContentMod) : ToClientMod() {

    override val screens = ToBaseScreenLibrary
    override val reloadListeners = ToBaseClientReloadListenerLibrary

}
