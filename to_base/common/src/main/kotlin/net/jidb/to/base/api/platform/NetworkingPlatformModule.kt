package net.jidb.to.base.api.platform

import net.jidb.to.base.api.network.PayloadEntry
import net.jidb.to.base.api.network.ServerPayloadContext
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.phys.Vec3

abstract class NetworkingPlatformModule {

    abstract fun <P : CustomPacketPayload> register(entry: PayloadEntry<P>, serverHandler: ((data: P, context: ServerPayloadContext) -> Unit)? = null)

    abstract fun sendToPlayer(player: ServerPlayer, payload: CustomPacketPayload, vararg others: CustomPacketPayload)
    open fun sendToPlayersNear(level: ServerLevel, pos: Vec3i, radius: Double, payload: CustomPacketPayload, vararg others: CustomPacketPayload, exclude: ServerPlayer? = null) = sendToPlayersNear(level, Vec3(pos), radius, payload, *others, exclude = exclude)
    abstract fun sendToPlayersNear(level: ServerLevel, pos: Vec3, radius: Double, payload: CustomPacketPayload, vararg others: CustomPacketPayload, exclude: ServerPlayer? = null)
    open fun sendToPlayersTrackingPos(level: ServerLevel, pos: BlockPos, payload: CustomPacketPayload, vararg others: CustomPacketPayload) = sendToPlayersTrackingChunk(level, ChunkPos.containing(pos), payload, *others)
    abstract fun sendToPlayersTrackingChunk(level: ServerLevel, pos: ChunkPos, payload: CustomPacketPayload, vararg others: CustomPacketPayload)
    abstract fun sendToPlayersTrackingEntity(entity: Entity, payload: CustomPacketPayload, vararg others: CustomPacketPayload)
    open fun sendToPlayersTrackingEntityAndSelf(entity: Entity, payload: CustomPacketPayload, vararg others: CustomPacketPayload) {
        sendToPlayersTrackingEntity(entity, payload, *others)
        if (entity is ServerPlayer) {
            sendToPlayer(entity, payload, *others)
        }
    }
    abstract fun sendToPlayerInDimension(level: ServerLevel, payload: CustomPacketPayload, vararg others: CustomPacketPayload)
    abstract fun sendToAll(server: MinecraftServer, payload: CustomPacketPayload, vararg others: CustomPacketPayload)
}
