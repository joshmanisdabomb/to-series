package net.jidb.to.base.api.mod

import org.slf4j.Logger

/**
 * Simple abstraction of a Minecraft cross-platform mod.
 * Includes a mod ID, logger, and [init] and [setup] steps with properties reporting their completion.
 *
 * @since 0.2.0
 */
interface IToMod {

    /**
     * The unique identifier for the mod.
     * This [String] is used to distinguish this mod's resources from other mods in the modded environment.
     * Preferable to use this in [net.jidb.to.base.pub.mod.ToMod] over a constant (e.g. MOD_ID) designed for @Mod and @EventBusSubscriber.
     *
     * @since 0.2.0
     */
    val modid: String

    /**
     * A logger instance for this mod, used to log messages and events during the lifecycle of the mod.
     * Feel free to use this logger for any purpose.
     *
     * @since 0.2.0
     */
    val logger: Logger

    /**
     * Whether this mod has been initialized, i.e. [init] has been called.
     *
     * @since 0.5.0
     */
    val initialised: Boolean

    /**
     * Whether this mod has been set up, i.e. [setup] has been called.
     *
     * @since 0.5.0
     */
    val complete: Boolean

    /**
     * This function should run when the mod is being initialized/constructed, to start registering the mod's items and blocks.
     * ToForgeMod will call this when the mod is constructed, while ToFabricMod will call this when the mod is initialised.
     *
     * @since 0.2.0
     */
    fun init() = Unit

    /**
     * This function should run later in the initialization lifecycle, after the mod is constructed and its items registered.
     * ToForgeMod will call this on the common setup event, while ToFabricMod will call this immediately after [init].
     *
     * @since 0.2.0
     */
    fun setup() = Unit

}
