package net.jidb.to.base.fabric.platform

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.jidb.to.base.network.PayloadEntry
import net.jidb.to.base.network.PayloadEntry.Phase
import net.jidb.to.base.network.PayloadEntry.Side
import net.jidb.to.base.network.ServerPayloadContext
import net.jidb.to.base.platform.NetworkingPlatformModule
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.phys.Vec3

object NetworkingFabricPlatformModule : NetworkingPlatformModule() {

    override fun <P : CustomPacketPayload> register(entry: PayloadEntry<P>, serverHandler: ((P, ServerPayloadContext) -> Unit)?) {
        if (entry.phase != Phase.PLAY) {
            if (entry.side != Side.C2S) PayloadTypeRegistry.clientboundConfiguration().register(entry.type, entry.codec)
            if (entry.side != Side.S2C) PayloadTypeRegistry.serverboundConfiguration().register(entry.type, entry.codec)
        }
        if (entry.phase != Phase.CONFIGURATION) {
            if (entry.side != Side.C2S) PayloadTypeRegistry.clientboundPlay().register(entry.type, entry.codec)
            if (entry.side != Side.S2C) PayloadTypeRegistry.serverboundPlay().register(entry.type, entry.codec)
        }
        if (entry.side != Side.S2C) {
            val handler = serverHandler ?: error("C2S payloads must have a server handler.")
            ServerPlayNetworking.registerGlobalReceiver(entry.type) { data, context -> handler(data, ServerPayloadContext(context.player())) }
        }
    }

    override fun sendToPlayer(player: ServerPlayer, payload: CustomPacketPayload, vararg others: CustomPacketPayload) {
        ServerPlayNetworking.send(player, payload)
        for (other in others) {
            ServerPlayNetworking.send(player, other)
        }
    }

    override fun sendToPlayersNear(level: ServerLevel, pos: Vec3, radius: Double, payload: CustomPacketPayload, vararg others: CustomPacketPayload, exclude: ServerPlayer?) {
        PlayerLookup.around(level, pos, radius).forEach {
            if (it == exclude) return@forEach
            sendToPlayer(it, payload, *others)
        }
    }

    override fun sendToPlayersNear(level: ServerLevel, pos: Vec3i, radius: Double, payload: CustomPacketPayload, vararg others: CustomPacketPayload, exclude: ServerPlayer?) {
        PlayerLookup.around(level, pos, radius).forEach {
            if (it == exclude) return@forEach
            sendToPlayer(it, payload, *others)
        }
    }

    override fun sendToPlayersTrackingPos(level: ServerLevel, pos: BlockPos, payload: CustomPacketPayload, vararg others: CustomPacketPayload) {
        PlayerLookup.tracking(level, pos).forEach {
            sendToPlayer(it, payload, *others)
        }
    }

    override fun sendToPlayersTrackingChunk(level: ServerLevel, pos: ChunkPos, payload: CustomPacketPayload, vararg others: CustomPacketPayload) {
        PlayerLookup.tracking(level, pos).forEach {
            sendToPlayer(it, payload, *others)
        }
    }

    override fun sendToPlayersTrackingEntity(entity: Entity, payload: CustomPacketPayload, vararg others: CustomPacketPayload) {
        PlayerLookup.tracking(entity).forEach {
            sendToPlayer(it, payload, *others)
        }
    }

    override fun sendToPlayerInDimension(level: ServerLevel, payload: CustomPacketPayload, vararg others: CustomPacketPayload) {
        PlayerLookup.level(level).forEach {
            sendToPlayer(it, payload, *others)
        }
    }

    override fun sendToAll(server: MinecraftServer, payload: CustomPacketPayload, vararg others: CustomPacketPayload) {
        PlayerLookup.all(server).forEach {
            sendToPlayer(it, payload, *others)
        }
    }

}
