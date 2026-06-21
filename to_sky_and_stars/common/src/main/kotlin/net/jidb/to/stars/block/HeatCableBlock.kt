package net.jidb.to.stars.block

import net.jidb.to.base.pub.block.NetworkCableBlock
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.network.HeatBlockNetworkType

class HeatCableBlock(properties: Properties) : NetworkCableBlock<HeatBlockNetworkType>(properties) {

    override val network by lazy { ToStarsMod.blockNetworks.heat }

    override val cableWidth = 10
    override val rimWidth = 1

}