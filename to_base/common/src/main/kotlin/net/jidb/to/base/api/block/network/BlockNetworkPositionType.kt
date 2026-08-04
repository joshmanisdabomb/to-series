package net.jidb.to.base.api.block.network

/**
 * Enum class representing the block's position type within a block network.
 *
 * This classification is used to differentiate between nodes, key position points within the network, and paths, positions that form routes between multiple nodes.
 *
 * @see BlockNetworkType.getPositionType
 * @since 0.6.0
 */
enum class BlockNetworkPositionType {

    /**
     * Nodes are key position points within a [BlockNetwork].
     *
     * Positions returning this type in [BlockNetworkType.getPositionType] will be added as nodes to a [BlockNetwork].
     *
     * @since 0.6.0
     */
    NODE,

    /**
     * Nodes are positions that form routes between multiple nodes.
     *
     * Positions returning this type in [BlockNetworkType.getPositionType] will be added as paths to a [BlockNetwork].
     *
     * @since 0.6.0
     */
    PATH

}
