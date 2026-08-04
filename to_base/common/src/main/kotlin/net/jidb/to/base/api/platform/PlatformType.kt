package net.jidb.to.base.api.platform

/**
 * Enum that defines the types of modloaders that the To Series support.
 * This is used to identify the current environment's modloader.
 *
 * @see Platform.type
 * @since 0.0.3
 */
enum class PlatformType {

    /**
     * Represents the Fabric modloader.
     *
     * @since 0.0.3
     */
    FABRIC,

    /**
     * Represents the Neoforge modloader.
     *
     * @since 0.0.3
     */
    NEOFORGE

}
