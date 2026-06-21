package net.jidb.to.stars.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.block.network.BlockNetworkType
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.network.HeatBlockNetworkType

object ToStarsBlockNetworkLibrary : SimpleRegistryLibrary<BlockNetworkType>(ToStarsMod.modid) {

    override val registry get() = ToBaseMod.registries.block_networks

    val heat by this { HeatBlockNetworkType() }
        .deferBuild(ToBaseMod::onInitialised)

}
