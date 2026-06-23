package net.jidb.to.base.api.side

import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

interface Side {

    val side: SideType

    fun getClientLevel(): Level?
    fun getClientPlayer(): Player?

    fun hasShiftDown(): Boolean
    fun hasAltDown(): Boolean
    fun hasControlDown(): Boolean

}