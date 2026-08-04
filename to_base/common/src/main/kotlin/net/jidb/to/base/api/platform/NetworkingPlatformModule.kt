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

/**
 * A [Platform]-specific module that defines a cross-platform contract for handling [CustomPacketPayload].
 * This module has code for registering and sending [CustomPacketPayload] between client and server.
 *
 * @since 0.2.0
 */
abstract class NetworkingPlatformModule {

    /**
     * Registers a [PayloadEntry] and an optional server-side handler for processing received payloads.
     *
     * @param P The type of the payload extending [CustomPacketPayload].
     * @param entry The payload entry containing metadata, codec, phase, and direction for handling the payload.
     * @param serverHandler An optional server-side handler function that processes the payload, taking the payload data [P] and [ServerPayloadContext] as parameters.
     * @since 0.2.0
     */
    abstract fun <P : CustomPacketPayload> register(entry: PayloadEntry<P>, serverHandler: ((data: P, context: ServerPayloadContext) -> Unit)? = null)

    /**
     * Sends one or more [CustomPacketPayload]s from the server to a specific [ServerPlayer].
     *
     * @param player The target player to whom the payload should be sent.
     * @param payload The primary [CustomPacketPayload] to send.
     * @param others Additional [CustomPacketPayload]s to send to the player, if any.
     * @since 0.2.0
     */
    abstract fun sendToPlayer(player: ServerPlayer, payload: CustomPacketPayload, vararg others: CustomPacketPayload)

    /**
     * Sends one or more [CustomPacketPayload]s from the server to all players near a specific position within a given radius.
     *
     * @param level The server level in which the players are located.
     * @param pos The central position from where the radius will be calculated to find nearby players.
     * @param radius The radius around the specified position within which players will receive the payloads.
     * @param payload The primary [CustomPacketPayload] to be sent to the nearby players.
     * @param others Additional [CustomPacketPayload]s to be included in the transmission, if any.
     * @param exclude An optional player to exclude from receiving the payloads (default is `null` i.e. no exclusions).
     * @return [Unit]
     * @since 0.2.0
     */
    open fun sendToPlayersNear(level: ServerLevel, pos: Vec3i, radius: Double, payload: CustomPacketPayload, vararg others: CustomPacketPayload, exclude: ServerPlayer? = null) = sendToPlayersNear(level, Vec3(pos), radius, payload, *others, exclude = exclude)

    /**
     * Sends one or more [CustomPacketPayload]s from the server to all players near a specific position within a given radius.
     *
     * @param level The server level in which the players are located.
     * @param pos The central position from where the radius will be calculated to find nearby players.
     * @param radius The radius around the specified position within which players will receive the payloads.
     * @param payload The primary [CustomPacketPayload] to be sent to the nearby players.
     * @param others Additional [CustomPacketPayload]s to be included in the transmission, if any.
     * @param exclude An optional player to exclude from receiving the payloads (default is `null` i.e. no exclusions).
     * @return [Unit]
     * @since 0.2.0
     */
    abstract fun sendToPlayersNear(level: ServerLevel, pos: Vec3, radius: Double, payload: CustomPacketPayload, vararg others: CustomPacketPayload, exclude: ServerPlayer? = null)

    /**
     * Sends one or more [CustomPacketPayload]s to all players tracking the block position (its chunk) containing the specified position.
     *
     * @param level The server level where the players are located.
     * @param pos The block position to use for identifying the associated chunk and its tracking players.
     * @param payload The primary [CustomPacketPayload] to send to the players.
     * @param others Additional [CustomPacketPayload]s to send to the players, if any.
     * @return [Unit]
     * @since 0.2.0
     */
    open fun sendToPlayersTrackingPos(level: ServerLevel, pos: BlockPos, payload: CustomPacketPayload, vararg others: CustomPacketPayload) = sendToPlayersTrackingChunk(level, ChunkPos.containing(pos), payload, *others)

    /**
     * Sends one or more [CustomPacketPayload]s from the server to all players tracking a specific chunk.
     *
     * @param level The server level where the chunk is located.
     * @param pos The position of the chunk whose tracking players will receive the payloads.
     * @param payload The primary [CustomPacketPayload] to send to the tracking players.
     * @param others Additional [CustomPacketPayload]s to send to the tracking players, if any.
     * @since 0.2.0
     */
    abstract fun sendToPlayersTrackingChunk(level: ServerLevel, pos: ChunkPos, payload: CustomPacketPayload, vararg others: CustomPacketPayload)

    /**
     * Sends one or more [CustomPacketPayload]s to all players currently tracking the specified [Entity].
     *
     * @param entity The entity whose tracking players will receive the payloads.
     * @param payload The primary [CustomPacketPayload] to send to the tracking players.
     * @param others Additional [CustomPacketPayload]s to send to the tracking players, if any.
     * @since 0.2.0
     */
    abstract fun sendToPlayersTrackingEntity(entity: Entity, payload: CustomPacketPayload, vararg others: CustomPacketPayload)

    /**
     * Sends one or more [CustomPacketPayload]s to all players tracking the specified [Entity].
     * If the [Entity] is a [ServerPlayer], sends the payloads to that player as well.
     *
     * @param entity The entity whose tracking players, and potentially player self, will receive the payloads.
     * @param payload The primary [CustomPacketPayload] to send.
     * @param others Additional [CustomPacketPayload]s to send, if any.
     * @since 0.2.0
     */
    open fun sendToPlayersTrackingEntityAndSelf(entity: Entity, payload: CustomPacketPayload, vararg others: CustomPacketPayload) {
        sendToPlayersTrackingEntity(entity, payload, *others)
        if (entity is ServerPlayer) {
            sendToPlayer(entity, payload, *others)
        }
    }

    /**
     * Sends one or more [CustomPacketPayload]s to all players present in the specified dimension.
     *
     * @param level The server level representing the dimension where the payloads should be sent.
     * @param payload The primary [CustomPacketPayload] to send to the players in the dimension.
     * @param others Additional [CustomPacketPayload]s to send to the players, if any.
     * @since 0.2.0
     */
    abstract fun sendToPlayerInDimension(level: ServerLevel, payload: CustomPacketPayload, vararg others: CustomPacketPayload)

    /**
     * Sends one or more [CustomPacketPayload]s from the server to all connected players, regardless of location.
     *
     * @param server The instance of the [MinecraftServer] managing the players and network connections.
     * @param payload The primary [CustomPacketPayload] to be sent to all players.
     * @param others Additional [CustomPacketPayload]s to send to all players, if any.
     * @since 0.2.0
     */
    abstract fun sendToAll(server: MinecraftServer, payload: CustomPacketPayload, vararg others: CustomPacketPayload)

}
