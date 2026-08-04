package net.jidb.to.base.api.side

/**
 * Enum that defines the physical sides that the game runs in.
 * This is used to identify the current environment's physical side of the network.
 *
 * @see Side.side
 * @since 0.6.0
 */
enum class SideType {

    /**
     * Represents the physical game client.
     *
     * Keep in mind the logical server could be running on this physical client for singleplayer or LAN.
     *
     * @since 0.6.0
     */
    CLIENT,

    /**
     * Represents the physical dedicated game server.
     *
     * Only the logical server can ever run here.
     *
     * @since 0.6.0
     */
    DEDICATED_SERVER

}
