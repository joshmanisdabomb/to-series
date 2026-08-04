package net.jidb.to.base.api.network

import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.Level

/**
 * A server-side implementation of [PayloadContext], for payloads received from the client and handled on the server.
 *
 * @property player The player who sent the payload to the server, with the covariant type [ServerPlayer].
 * @since 0.2.0
 */
data class ServerPayloadContext(override val player: ServerPlayer) : PayloadContext(player) {

    /**
     * The [ServerLevel] in which the associated [ServerPlayer] is located.
     * [ServerLevel] is a covariant type from [Level].
     *
     * @since 0.2.0
     */
    override val level: ServerLevel get() = player.level()

}
