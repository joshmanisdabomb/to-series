package net.jidb.to.base.service

import net.jidb.to.base.api.platform.Platform
import net.jidb.to.base.api.side.Side

/**
 * A service that describes the environment the game is currently running under, i.e. which modloader, which physical side, and which other mods are present.
 * Each loader project provides its own implementation, loaded through [Services].
 *
 * Usually accessed from the [Services] object:
 * ```kotlin
 * Services.environment
 * ```
 *
 * @since 0.0.3
 */
abstract class EnvironmentService {

    /**
     * The [Platform] for the modloader currently running, which exposes the cross-platform modules for loader-specific code.
     *
     * @since 0.0.3
     */
    abstract val platform: Platform

    /**
     * Whether the game is running from a development environment or from a built jar.
     *
     * @since 0.0.3
     */
    abstract val environment: Environment

    /**
     * The [Side] for the physical side currently running, i.e. client or server.
     *
     * @since 0.6.0
     */
    abstract val side: Side

    /**
     * Checks whether another mod is currently loaded, which is the cross-platform equivalent of asking either modloader's own mod list.
     *
     * @param modId The mod ID to look for.
     * @return Returns `true` if a mod with the given ID is loaded, `false` otherwise.
     * @since 0.0.3
     */
    abstract fun isModLoaded(modId: String): Boolean

    /**
     * Retrieves the version string another mod declares in its metadata.
     *
     * @param modId The mod ID to look for.
     * @return The version of the mod with the given ID, or `null` if it is not loaded.
     * @since 0.8.0
     */
    abstract fun getModVersion(modId: String): String?

    /**
     * Enum that defines the kinds of environment the game can be launched from.
     * This is used to enable behaviour, such as test content, that should only exist while developing.
     *
     * @see EnvironmentService.environment
     * @since 0.0.3
     */
    enum class Environment {

        /**
         * Represents a development environment, i.e. the game launched from Gradle or an IDE run config.
         *
         * @since 0.0.3
         */
        DEV,

        /**
         * Represents a production environment, i.e. the game launched with the mod as a built jar.
         *
         * @since 0.0.3
         */
        BUILD

    }

}
