package net.jidb.to.base.pub.event.level

import net.minecraft.server.level.ServerLevel

/**
 * The context given to a handler of a server level event, naming the level it concerns.
 *
 * @property level The server level the event fired for.
 * @since 0.6.0
 */
data class ServerLevelEventContext(val level: ServerLevel)
