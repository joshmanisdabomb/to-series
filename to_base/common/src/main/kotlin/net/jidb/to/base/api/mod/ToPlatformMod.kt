package net.jidb.to.base.api.mod

/**
 * Represents a platform-specific abstraction for a cross-platform [IToMod].
 * This is the interface that a Fabric or Forge mod would implement, and the common mod code would be encapsulated in [common] and the [init] and [setup] hooks passed down to it.
 *
 * @since 0.2.0
 */
interface ToPlatformMod {

    /**
     * The common code of this platform-specific mod container.
     * This is where the content of your mod would be, independent of the current modloader.
     *
     * @since 0.2.0
     */
    val common: IToMod

    /**
     * This function should run when the mod is being initialized/constructed, to make a call to [IToMod.init]
     * ToForgeMod will call this when the mod is constructed, while ToFabricMod will call this when the mod is initialized.
     *
     * @since 0.2.0
     */
    fun init() = Unit

    /**
     * This function should run later in the initialization lifecycle, to make a call to [IToMod.setup]
     * ToForgeMod will call this on the common setup event, while ToFabricMod will call this immediately after [init].
     *
     * @since 0.2.0
     */
    fun setup() = Unit

}
