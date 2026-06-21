package net.jidb.to.base.pub.block.network.energy

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.block.network.BlockNetworkType
import net.jidb.to.base.api.library.SimpleRegistryLibrary

object ToBaseBlockNetworkLibrary : SimpleRegistryLibrary<BlockNetworkType>(ToBaseMod.modid) {

    override val registry get() = ToBaseMod.registries.block_networks

    val to_energy by this { ToEnergyBlockNetworkType() }

}