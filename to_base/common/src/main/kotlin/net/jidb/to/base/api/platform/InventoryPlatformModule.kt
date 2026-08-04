package net.jidb.to.base.api.platform

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

/**
 * A [Platform]-specific module that defines a cross-platform contract for handling [AbstractContainerMenu]s and [net.minecraft.world.Container]s.
 * This module has code for registering a [MenuType] and sending additional data from a server to client when the [AbstractContainerMenu] is opened.
 *
 * @since 0.1.0
 */
abstract class InventoryPlatformModule {

    /**
     * Creates a basic [MenuType] with the provided constructor.
     * This [AbstractContainerMenu] will not be sent any additional data from the server when opened by the player.
     *
     * @param T The type of [AbstractContainerMenu] represented by the [MenuType].
     * @param constructor A function that constructs a menu instance, given an ID and the player's inventory.
     * @param features The set of feature flags to register with this menu type. Defaults to [FeatureFlags.DEFAULT_FLAGS].
     * @return A new [MenuType] instance, unregistered.
     * @since 0.8.0
     */
    abstract fun <T : AbstractContainerMenu> createBasicMenu(constructor: (id: Int, playerInventory: Inventory) -> T, features: FeatureFlagSet = FeatureFlags.DEFAULT_FLAGS): MenuType<T>

    /**
     * Creates an "extended" [MenuType] with the provided constructor.
     * This [AbstractContainerMenu] will be sent additional data from the server when opened by the player via [openExtendedMenu].
     *
     * @param T The type of [AbstractContainerMenu] represented by the [MenuType].
     * @param D The custom data type that will be encoded and decoded from server to client when the menu is opened.
     * @param constructor A function that constructs a menu instance, given an ID, the player's inventory, and custom data [D].
     * @param codec The codec that handles encoding and decoding of custom data [D] across the network via a [RegistryFriendlyByteBuf].
     * @return A new [MenuType] instance, unregistered.
     * @since 0.8.0
     */
    abstract fun <T : AbstractContainerMenu, D : Any> createExtendedMenu(constructor: (id: Int, playerInventory: Inventory, data: D) -> T, codec: StreamCodec<in RegistryFriendlyByteBuf, D>): MenuType<T>

    /**
     * Opens an "extended" menu for the specified player, allowing custom data to be transmitted from the server to the client once opened.
     *
     * @param T The type of [AbstractContainerMenu] represented by the [MenuType].
     * @param D The custom data type that will be encoded and decoded from server to client when the menu is opened.
     * @param player The player for whom the menu is being opened.
     * @param type The [MenuType] instance representing the type of menu to be opened.
     * @param original The original [MenuProvider], still used to create the actual [AbstractContainerMenu] and provide the display name.
     * @param data The custom data to be sent along with the menu to the client.
     * @since 0.8.0
     */
    abstract fun <T : AbstractContainerMenu, D : Any> openExtendedMenu(player: Player, type: MenuType<T>, original: MenuProvider?, data: D)

}
