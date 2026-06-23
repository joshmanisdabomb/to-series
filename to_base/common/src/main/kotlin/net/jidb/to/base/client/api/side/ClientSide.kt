package net.jidb.to.base.client.api.side

import net.jidb.to.base.api.side.Side
import net.jidb.to.base.api.side.SideType
import net.minecraft.client.Minecraft

class ClientSide : Side {

    val minecraft get() = Minecraft.getInstance()

    override val side = SideType.CLIENT

    override fun getClientLevel() = minecraft.level
    override fun getClientPlayer() = minecraft.player

    override fun hasShiftDown() = minecraft.hasShiftDown()
    override fun hasAltDown() = minecraft.hasAltDown()
    override fun hasControlDown() = minecraft.hasControlDown()

}
