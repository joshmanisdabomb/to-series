package net.jidb.to.base.fabric.platform

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType
import net.jidb.to.base.api.platform.InventoryPlatformModule
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

object InventoryFabricPlatformModule : InventoryPlatformModule() {

    override fun <T : AbstractContainerMenu> createBasicMenu(constructor: (id: Int, playerInventory: Inventory) -> T, features: FeatureFlagSet) = MenuType(constructor, features)

    override fun <T : AbstractContainerMenu, D : Any> createExtendedMenu(constructor: (id: Int, playerInventory: Inventory, data: D) -> T, codec: StreamCodec<in RegistryFriendlyByteBuf, D>) = ExtendedMenuType(constructor, codec)

    override fun <T : AbstractContainerMenu, D : Any> openExtendedMenu(player: Player, type: MenuType<T>, original: MenuProvider?, data: D) {
        if (original == null) return
        player.openMenu(object : ExtendedMenuProvider<D> {

            override fun getScreenOpeningData(player: ServerPlayer) = data

            override fun getDisplayName() = original.displayName

            override fun createMenu(containerId: Int, inventory: Inventory, player: Player) = original.createMenu(containerId, inventory, player)

        })
    }

}
