package net.jidb.to.base.neoforge.platform

import net.jidb.to.base.network.PayloadEntry
import net.jidb.to.base.network.ServerPayloadContext
import net.jidb.to.base.platform.NetworkingPlatformModule
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.handling.IPayloadContext
import net.neoforged.neoforge.network.registration.PayloadRegistrar

object NetworkingForgePlatformModule : NetworkingPlatformModule() {

    val registry = DeferredForgeEventRegistry(RegisterPayloadHandlersEvent::class.java)
    val registrars = mutableMapOf<String, MutableMap<String, PayloadRegistrar>>()

    override fun <P : CustomPacketPayload> register(entry: PayloadEntry<P>, serverHandler: ((P, ServerPayloadContext) -> Unit)?) {
        registry.register(entry.type.id.namespace) { event ->
            val registrar = registrars.getOrPut(entry.type.id.namespace) { mutableMapOf() }.getOrPut(entry.registrar) { event.registrar(entry.registrar) }
            when (entry.side) {
                PayloadEntry.Side.C2S -> {
                    val handler = createForgeServerHandler(serverHandler)
                    when (entry.phase) {
                        PayloadEntry.Phase.PLAY -> registrar.playToServer(entry.type, entry.codec, handler)
                        PayloadEntry.Phase.CONFIGURATION -> registrar.configurationToServer(entry.type, entry.codec, handler)
                        PayloadEntry.Phase.COMMON -> registrar.commonToServer(entry.type, entry.codec, handler)
                    }
                }
                PayloadEntry.Side.S2C -> when (entry.phase) {
                    PayloadEntry.Phase.PLAY -> registrar.playToClient(entry.type, entry.codec)
                    PayloadEntry.Phase.CONFIGURATION -> registrar.configurationToClient(entry.type, entry.codec)
                    PayloadEntry.Phase.COMMON -> registrar.commonToClient(entry.type, entry.codec)
                }
                PayloadEntry.Side.BIDIRECTIONAL -> {
                    val handler = createForgeServerHandler(serverHandler)
                    when (entry.phase) {
                        PayloadEntry.Phase.PLAY -> registrar.playBidirectional(entry.type, entry.codec, handler)
                        PayloadEntry.Phase.CONFIGURATION -> registrar.playBidirectional(entry.type, entry.codec, handler)
                        PayloadEntry.Phase.COMMON -> registrar.commonBidirectional(entry.type, entry.codec, handler)
                    }
                }
            }
        }
    }

    private fun <P : CustomPacketPayload> createForgeServerHandler(handler: ((data: P, context: ServerPayloadContext) -> Unit)? = null): ((data: P, context: IPayloadContext) -> Unit) {
        val ret = handler ?: error("C2S payloads must have a server handler.")
        return { data, context -> ret(data, ServerPayloadContext(context.player() as ServerPlayer)) }
    }

    override fun sendToPlayer(player: ServerPlayer, payload: CustomPacketPayload, vararg others: CustomPacketPayload) = PacketDistributor.sendToPlayer(player, payload, *others)
    override fun sendToPlayersNear(level: ServerLevel, pos: Vec3, radius: Double, payload: CustomPacketPayload, vararg others: CustomPacketPayload, exclude: ServerPlayer?) = PacketDistributor.sendToPlayersNear(level, exclude, pos.x, pos.y, pos.z, radius, payload, *others)
    override fun sendToPlayersTrackingChunk(level: ServerLevel, pos: ChunkPos, payload: CustomPacketPayload, vararg others: CustomPacketPayload) = PacketDistributor.sendToPlayersTrackingChunk(level, pos, payload, *others)
    override fun sendToPlayersTrackingEntity(entity: Entity, payload: CustomPacketPayload, vararg others: CustomPacketPayload) = PacketDistributor.sendToPlayersTrackingEntity(entity, payload, *others)
    override fun sendToPlayersTrackingEntityAndSelf(entity: Entity, payload: CustomPacketPayload, vararg others: CustomPacketPayload) = PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, payload, *others)
    override fun sendToPlayerInDimension(level: ServerLevel, payload: CustomPacketPayload, vararg others: CustomPacketPayload) = PacketDistributor.sendToPlayersInDimension(level, payload, *others)
    override fun sendToAll(server: MinecraftServer, payload: CustomPacketPayload, vararg others: CustomPacketPayload) = PacketDistributor.sendToAllPlayers(payload, *others)

}
