package net.jidb.to.base.neoforge.client.data.mod

import net.neoforged.neoforge.data.event.GatherDataEvent

/**
 * The bare shape of a mod's data generation on Neoforge: the event it is being run from, and the call that registers every provider against it.
 *
 * @since 0.3.0
 */
interface IToForgeDataMod {

    /**
     * The event the providers are registered against, which is raised only on the data generation run.
     *
     * @since 0.3.0
     */
    val event: GatherDataEvent.Client

    /**
     * Registers every provider this mod generates through.
     *
     * @since 0.3.0
     */
    fun generate()

}
