package net.jidb.to.base.pub.block.network.energy

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.block.network.BlockNetworkType
import net.jidb.to.base.api.library.SimpleRegistryLibrary

/**
 * [SimpleRegistryLibrary] implementation that registers the [BlockNetworkType] content of To Lay the Foundations, and provides access to it in one place.
 * The registry itself is one the mod creates rather than a vanilla one, which is why it is read from [net.jidb.to.base.pub.ToBaseRegistryLibrary] rather than named directly.
 *
 * @since 0.6.0
 */
object ToBaseBlockNetworkLibrary : SimpleRegistryLibrary<BlockNetworkType>(ToBaseMod.modid) {

    override val registry get() = ToBaseMod.registries.block_networks

    /**
     * The network that carries To Energy between cables and the machines attached to them.
     *
     * @since 0.6.0
     */
    val to_energy by this { ToEnergyBlockNetworkType() }

}
