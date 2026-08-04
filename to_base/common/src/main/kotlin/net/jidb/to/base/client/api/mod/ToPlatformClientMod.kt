package net.jidb.to.base.client.api.mod

/**
 * Represents a platform-specific abstraction for a cross-platform client-side [IToClientMod].
 * This is the interface that a Fabric or Forge client mod would implement, and the common client mod code would be encapsulated in [client] and the [clientInit] and [clientSetup] hooks passed down to it.
 *
 * @since 0.2.0
 */
interface ToPlatformClientMod {

    /**
     * The common client-side code of this platform-specific mod container.
     * This is where the client content of your mod would be, independent of the current modloader.
     *
     * @since 0.2.0
     */
    val client: IToClientMod

    /**
     * This function should run when the client mod is being initialized/constructed, to make a call to [IToClientMod.clientInit]
     * ToForgeClientMod will call this when the mod is constructed, while ToFabricClientMod will call this when the mod is initialised.
     *
     * @since 0.2.0
     */
    fun clientInit() = Unit

    /**
     * This function should run later in the initialization lifecycle, to make a call to [IToClientMod.clientSetup]
     * ToForgeClientMod will call this on the client setup event, while ToFabricClientMod will call this immediately after [clientInit].
     *
     * @since 0.2.0
     */
    fun clientSetup() = Unit

}
