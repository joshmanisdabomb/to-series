package net.jidb.to.base.neoforge.client.platform

import net.jidb.to.base.client.api.network.ClientPayloadContext
import net.jidb.to.base.client.api.platform.NetworkingClientPlatformModule
import net.jidb.to.base.neoforge.platform.DeferredForgeEventRegistry
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.client.network.ClientPacketDistributor
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent

/**
 * [NetworkingClientPlatformModule] implementation for Neoforge.
 *
 * A handler cannot be registered whenever a mod asks, only while Neoforge is raising its own event, so every declaration is queued in a [DeferredForgeEventRegistry] and played back once that arrives.
 *
 * @since 0.2.0
 */
object NetworkingForgeClientPlatformModule : NetworkingClientPlatformModule() {

    /**
     * The queued registrations, played back when Neoforge raises the event they are waiting on.
     *
     * @since 0.2.0
     */
    val registry = DeferredForgeEventRegistry(RegisterClientPayloadHandlersEvent::class.java)

    override fun <P : CustomPacketPayload> registerHandler(type: CustomPacketPayload.Type<P>, clientHandler: (P, ClientPayloadContext) -> Unit) {
        registry.register(type.id.namespace) { event ->
            event.register(type) { data, context -> clientHandler(data, ClientPayloadContext(context.player() as LocalPlayer)) }
        }
    }

    override fun sendToServer(payload: CustomPacketPayload, vararg others: CustomPacketPayload) {
        ClientPacketDistributor.sendToServer(payload, *others)
    }

}
