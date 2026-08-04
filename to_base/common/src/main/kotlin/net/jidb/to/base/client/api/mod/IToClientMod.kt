package net.jidb.to.base.client.api.mod

import net.jidb.to.base.api.mod.IToMod

/**
 * Simple abstraction of a Minecraft cross-platform client-side mod.
 * Includes the [common] mod, logger, and [clientInit] and [clientSetup] steps with properties reporting their completion.
 *
 * @since 0.2.0
 */
interface IToClientMod {

    /**
     * The common mod container that this client mod is associated with.
     *
     * @since 0.2.0
     */
    val common: IToMod

    /**
     * Whether this client mod has been initialized, i.e. [clientInit] has been called.
     *
     * @since 0.5.0
     */
    val initialised: Boolean

    /**
     * Whether this client mod has been set up, i.e. [clientSetup] has been called.
     *
     * @since 0.5.0
     */
    val complete: Boolean

    /**
     * This function should run when the client-side part of the mod is being initialized/constructed.
     * ToForgeClientMod will call this when the mod is constructed, while ToFabricClientMod will call this when the mod is initialised.
     *
     * @since 0.2.0
     */
    fun clientInit() = Unit

    /**
     * This function should run later in the initialization lifecycle, after the client-side mod is constructed.
     * ToForgeClientMod will call this on the client setup event, while ToFabricClientMod will call this immediately after [clientIni].
     *
     * @since 0.2.0
     */
    fun clientSetup() = Unit

}
