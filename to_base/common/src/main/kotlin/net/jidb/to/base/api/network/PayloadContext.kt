package net.jidb.to.base.api.network

import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

/**
 * A context class used for passing data to a payload handler, when a registered [net.minecraft.network.protocol.common.custom.CustomPacketPayload] reaches the other side.
 * It currently includes the [Player] who sent/received the packet, and its [Level].
 *
 * @property player The player who sent/received the payload.
 * @since 0.2.0
 */
open class PayloadContext(open val player: Player) {

    /**
     * The [Level] in which the associated [Player] is located.
     *
     * @since 0.2.0
     */
    open val level: Level get() = player.level()

}
