package net.jidb.to.base.neoforge.client.service

import net.neoforged.neoforge.data.event.GatherDataEvent

/**
 * The service a mod registers to say that it generates data, and to run the generation when asked.
 *
 * This is the whole of the seam between a mod and its own data generation. The generator packages are stripped from a built jar, so nothing that ships may name what is in them; an implementation of this service lives in those packages alongside the generator it builds, and is reached only through [java.util.ServiceLoader], which resolves nothing until [net.jidb.to.base.neoforge.client.mod.ToForgeClientMod.onGatherData] asks it to.
 * The `META-INF/services` entry naming the implementation is stripped from a built jar for the same reason.
 *
 * Registered per mod rather than per loader, unlike the services under [net.jidb.to.base.service.Services], so [modid] says which mod each implementation speaks for.
 *
 * @since 1.0.0
 */
abstract class ForgeDataModService {

    /**
     * The id of the mod this generates the data of, which is what an implementation is matched to a mod by.
     *
     * @since 1.0.0
     */
    abstract val modid: String

    /**
     * Builds this mod's data generator and registers every provider it has against the event.
     * Only ever called on a data generation run, which is the only time the generator packages are present.
     *
     * @param event The event the providers are registered against.
     * @since 1.0.0
     */
    abstract fun generate(event: GatherDataEvent.Client)

}
