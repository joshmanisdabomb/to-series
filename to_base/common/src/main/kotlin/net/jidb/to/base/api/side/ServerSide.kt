package net.jidb.to.base.api.side

class ServerSide : Side {

    override val side = SideType.DEDICATED_SERVER

    override fun getClientLevel() = null
    override fun getClientPlayer() = null

    override fun hasShiftDown() = false
    override fun hasAltDown() = false
    override fun hasControlDown() = false

}