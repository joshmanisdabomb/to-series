package net.jidb.to.base.api.side

class ServerSide : Side {

    override val side = SideType.DEDICATED_SERVER

    override fun hasShiftDown() = false

}