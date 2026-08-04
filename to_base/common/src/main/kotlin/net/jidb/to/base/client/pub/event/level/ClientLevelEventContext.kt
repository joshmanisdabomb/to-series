package net.jidb.to.base.client.pub.event.level

import net.minecraft.client.multiplayer.ClientLevel

/**
 * The context given to a handler of a client level event, naming the level it concerns.
 *
 * @property level The client level the event fired for.
 * @since 0.8.0
 */
data class ClientLevelEventContext(val level: ClientLevel)
