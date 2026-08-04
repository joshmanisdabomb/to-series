package net.jidb.to.base.api.platform

/**
 * Interface that represents a modloader, such as Neoforge and Fabric.
 * This class contains properties representing 'modules', that are themselves abstract classes that run platform-specific code that can be run independent of the current modloader.
 *
 * Usually accessed from the [net.jidb.to.base.service.Services] object:
 * ```kotlin
 * Services.platform
 * ```
 *
 * Use [type] if you just want to find out which modloader is currently being used.
 * See [net.jidb.to.base.client.api.platform.ClientPlatform] for platform-specific code that is client side.
 *
 * @since 0.0.3
 */
interface Platform {

    /**
     * The type of modloader in use, such as Fabric or Neoforge.
     *
     * @since 0.0.3
     */
    val type: PlatformType

    /**
     * Provides cross-platform code around [net.minecraft.world.level.block.Block], [net.minecraft.world.level.block.entity.BlockEntity] and other block code.
     *
     * @since 0.1.0
     */
    val blocks: BlocksPlatformModule

    /**
     * Provides cross-platform code around building a [net.minecraft.world.item.CreativeModeTab].
     *
     * @since 0.0.3
     */
    val creativeTabs: CreativeTabsPlatformModule

    /**
     * Provides cross-platform code around common `c:` tags provided by both Neoforge and Fabric.
     *
     * @since 0.1.0
     */
    val tags: TagsPlatformModule

    /**
     * Provides cross-platform code around creating a [net.minecraft.world.inventory.MenuType] and sending custom data from server to client when a [net.minecraft.world.inventory.AbstractContainerMenu] is opened.
     *
     * @since 0.1.0
     */
    val inventory: InventoryPlatformModule

    /**
     * Provides cross-platform code around sending a [net.minecraft.network.protocol.common.custom.CustomPacketPayload] between client and server, as well as registering payload handlers.
     *
     * @since 0.2.0
     */
    val networking: NetworkingPlatformModule

    /**
     * Provides cross-platform code around registering custom biome modifications to existing vanilla biomes, such as adding a new [net.minecraft.world.level.levelgen.feature.ConfiguredFeature] to the overworld via [net.jidb.to.base.api.level.biome.BiomeMod].
     *
     * @since 0.3.0
     */
    val biomes: BiomePlatformModule

    /**
     * Provides cross-platform code around registering a reload listener that runs on game load and F3+R refresh.
     *
     * @since 0.2.0
     */
    val reloadListeners: ReloadListenerPlatformModule

    /**
     * Provides cross-platform code to use the Neoforge and Fabric transfer systems.
     *
     * @since 0.6.0
     */
    val transfer: TransferPlatformModule

}
