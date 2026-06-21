package net.jidb.to.base.api.side

interface Side {

    val side: SideType

    fun hasShiftDown(): Boolean

}