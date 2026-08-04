package net.jidb.to.base.client.api.platform

/**
 * Interface that holds client-side 'modules' for a modloader.
 * These modules are themselves abstract classes that run platform-specific code that can be run independent of the current modloader.
 *
 * Usually accessed from the [net.jidb.to.base.client.service.ClientServices] object:
 * ```kotlin
 * ClientServices.platform
 * ```
 *
 * @see net.jidb.to.base.api.platform.Platform
 * @since 0.2.0
 */
interface ClientPlatform {

    /**
     * Provides cross-platform code around registering a [net.minecraft.client.renderer.blockentity.BlockEntityRenderer] for a [net.minecraft.world.level.block.entity.BlockEntityType].
     *
     * @since 0.4.0
     */
    val blocks: BlocksClientPlatformModule

    /**
     * Provides cross-platform code around registering a [net.minecraft.client.renderer.entity.EntityRenderer] for an [net.minecraft.world.entity.EntityType].
     *
     * @since 0.4.0
     */
    val entities: EntitiesClientPlatformModule

    /**
     * Provides cross-platform code around model layers, special model renderers and the tint sources that colour a block or item.
     *
     * @since 0.6.0
     */
    val models: ModelsClientPlatformModule

    /**
     * Provides cross-platform code around registering a [net.minecraft.client.particle.ParticleProvider] for a [net.minecraft.core.particles.ParticleType].
     *
     * @since 0.2.0
     */
    val particles: ParticleClientPlatformModule

    /**
     * Provides cross-platform code around binding a [net.minecraft.client.gui.screens.Screen] to the [net.minecraft.world.inventory.MenuType] it opens for.
     *
     * @since 0.2.0
     */
    val screens: ScreenClientPlatformModule

    /**
     * Provides cross-platform code around sending a [net.minecraft.network.protocol.common.custom.CustomPacketPayload] to the server, as well as registering the handlers that receive one on the client.
     *
     * @since 0.2.0
     */
    val networking: NetworkingClientPlatformModule

    /**
     * Provides cross-platform code around registering a reload listener that runs against the client's resource packs rather than the server's data packs.
     *
     * @since 0.2.0
     */
    val reloadListeners: ReloadListenerClientPlatformModule

    /**
     * Provides cross-platform code around the client-side data generators, which build models and language files.
     *
     * @since 0.4.0
     */
    val data: DataClientPlatformModule

}
