package net.jidb.to.base.client.api.network

import net.jidb.to.base.api.network.PayloadContext
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.level.Level

/**
 * A client-side implementation of [PayloadContext], for payloads received from the server and handled on the client.
 *
 * @property player The player who received the payload from the server, with the covariant type [LocalPlayer].
 * @since 0.2.0
 */
data class ClientPayloadContext(override val player: LocalPlayer) : PayloadContext(player) {

    /**
     * The [ClientLevel] in which the associated [LocalPlayer] is located.
     * [ClientLevel] is a covariant type from [Level].
     *
     * @since 0.2.0
     */
    override val level: ClientLevel get() = player.level() as ClientLevel

}
