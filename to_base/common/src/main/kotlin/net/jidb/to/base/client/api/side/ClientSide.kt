package net.jidb.to.base.client.api.side

import net.jidb.to.base.api.side.Side
import net.jidb.to.base.api.side.SideType
import net.minecraft.client.Minecraft

/**
 * The [Side] implementation for the physical client, where every question a [Side] can be asked has a real answer.
 * Its counterpart is [net.jidb.to.base.api.side.ServerSide], which returns the sensible fallback for each of them instead.
 *
 * @see Side
 * @since 0.6.0
 */
class ClientSide : Side {

    /**
     * The running [Minecraft] client instance, which every other member of this class answers from.
     *
     * @since 0.6.0
     */
    val minecraft get() = Minecraft.getInstance()

    override val side = SideType.CLIENT

    override fun getClientLevel() = minecraft.level
    override fun getClientPlayer() = minecraft.player

    override fun hasShiftDown() = minecraft.hasShiftDown()
    override fun hasAltDown() = minecraft.hasAltDown()
    override fun hasControlDown() = minecraft.hasControlDown()

}
