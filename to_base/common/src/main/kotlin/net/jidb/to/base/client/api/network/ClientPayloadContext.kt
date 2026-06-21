package net.jidb.to.base.client.api.network

import net.jidb.to.base.api.network.PayloadContext
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer

data class ClientPayloadContext(override val player: LocalPlayer) : PayloadContext(player) {
    override val level: ClientLevel get() = player.level() as ClientLevel
}