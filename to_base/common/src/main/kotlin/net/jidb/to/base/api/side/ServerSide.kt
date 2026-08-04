package net.jidb.to.base.api.side

/**
 * Physical server-side implementation of the [Side] interface representing the dedicated server side.
 *
 * Returns sensible fallback values for [Side] methods intended to be invoked on the client.
 *
 * @since 0.6.0
 */
class ServerSide : Side {

    override val side = SideType.DEDICATED_SERVER

    override fun getClientLevel() = null
    override fun getClientPlayer() = null

    override fun hasShiftDown() = false
    override fun hasAltDown() = false
    override fun hasControlDown() = false

}
