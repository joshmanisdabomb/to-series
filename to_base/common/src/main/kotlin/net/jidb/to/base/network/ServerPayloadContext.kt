package net.jidb.to.base.network

import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

data class ServerPayloadContext(override val player: ServerPlayer) : PayloadContext(player) {
    override val level: ServerLevel get() = player.level()
}
