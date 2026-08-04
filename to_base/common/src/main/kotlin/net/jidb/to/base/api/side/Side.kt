package net.jidb.to.base.api.side

import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

/**
 * Interface that represents a physical side of the network, i.e. client or server.
 * This class contains methods that give the answer on the correct side, but a sensible fallback on the incorrect side.
 *
 * Usually accessed from the [net.jidb.to.base.service.Services] object:
 * ```kotlin
 * Services.side
 * ```
 *
 * Use [side] if you just want to find out which side is currently being used.
 *
 * @since 0.6.0
 */
interface Side {

    /**
     * The type of physical side in use, i.e. client or server.
     *
     * @since 0.6.0
     */
    val side: SideType

    /**
     * Retrieves the current [Level] from the client, or null.
     *
     * @return the [net.minecraft.client.multiplayer.ClientLevel] instance as [Level] on the client side, or null if invoked on the server side.
     * @since 0.7.0
     */
    fun getClientLevel(): Level?

    /**
     * Retrieves the current [Player] from the client, or null.
     *
     * @return the [net.minecraft.client.player.LocalPlayer] instance as [Player] on the client side, or null if invoked on the server side.
     * @since 0.7.0
     */
    fun getClientPlayer(): Player?

    /**
     * Checks if the current user is holding down Shift on the client.
     *
     * @since 0.6.0
     * @return `true` if shift is down, `false` otherwise or if invoked on the server.
     */
    fun hasShiftDown(): Boolean

    /**
     * Checks if the current user is holding down Alt on the client.
     *
     * @since 0.7.0
     * @return `true` if alt is down, `false` otherwise or if invoked on the server.
     */
    fun hasAltDown(): Boolean

    /**
     * Checks if the current user is holding down Ctrl on the client.
     *
     * @since 0.7.0
     * @return `true` if control is down, `false` otherwise or if invoked on the server.
     */
    fun hasControlDown(): Boolean

}
