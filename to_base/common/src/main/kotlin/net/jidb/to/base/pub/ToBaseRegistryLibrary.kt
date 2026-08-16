package net.jidb.to.base.pub

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.block.network.BlockNetworkType
import net.jidb.to.base.api.library.RegistryRegistryLibrary

object ToBaseRegistryLibrary : RegistryRegistryLibrary(ToBaseMod.modid) {

    val block_networks by this<BlockNetworkType>()

}
