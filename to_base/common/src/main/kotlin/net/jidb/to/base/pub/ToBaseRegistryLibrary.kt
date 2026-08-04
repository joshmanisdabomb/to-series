package net.jidb.to.base.pub

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.block.network.BlockNetworkType
import net.jidb.to.base.api.library.RegistryRegistryLibrary

/**
 * [RegistryRegistryLibrary] implementation that creates the registries To Lay the Foundations adds, and provides access to them in one place.
 * These are registries of the mod's own rather than vanilla ones, so they have to be created before anything can be registered into them.
 *
 * @since 0.6.0
 */
object ToBaseRegistryLibrary : RegistryRegistryLibrary(ToBaseMod.modid) {

    /**
     * The registry holding every [BlockNetworkType], i.e. each kind of thing that can travel between connected blocks.
     *
     * @since 0.6.0
     */
    val block_networks by this<BlockNetworkType>()

}
