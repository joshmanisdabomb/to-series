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

abstract class InventoryPlatformModule {

    abstract fun <T : AbstractContainerMenu> createBasicMenu(constructor: (id: Int, playerInventory: Inventory) -> T, features: FeatureFlagSet = FeatureFlags.DEFAULT_FLAGS): MenuType<T>

    abstract fun <T : AbstractContainerMenu, D : Any> createExtendedMenu(constructor: (id: Int, playerInventory: Inventory, data: D) -> T, codec: StreamCodec<in RegistryFriendlyByteBuf, D>): MenuType<T>

    abstract fun <T : AbstractContainerMenu, D : Any> openExtendedMenu(player: Player, type: MenuType<T>, original: MenuProvider?, data: D)

}
