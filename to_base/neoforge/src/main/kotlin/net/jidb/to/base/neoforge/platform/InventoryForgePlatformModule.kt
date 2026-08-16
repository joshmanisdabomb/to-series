package net.jidb.to.base.neoforge.platform

import net.jidb.to.base.api.platform.InventoryPlatformModule
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.common.extensions.IPlayerExtension

object InventoryForgePlatformModule : InventoryPlatformModule() {

    private val extendedCodecs = mutableMapOf<MenuType<*>, StreamCodec<in RegistryFriendlyByteBuf, *>>()

    override fun <T : AbstractContainerMenu> createBasicMenu(constructor: (id: Int, playerInventory: Inventory) -> T, features: FeatureFlagSet) = MenuType(constructor, features)

    override fun <T : AbstractContainerMenu, D : Any> createExtendedMenu(constructor: (id: Int, playerInventory: Inventory, data: D) -> T, codec: StreamCodec<in RegistryFriendlyByteBuf, D>): MenuType<T> {
        val type = IMenuTypeExtension.create { id, playerInventory, buf -> constructor(id, playerInventory, codec.decode(buf)) }
        extendedCodecs[type] = codec
        return type
    }

    override fun <T : AbstractContainerMenu, D : Any> openExtendedMenu(player: Player, type: MenuType<T>, original: MenuProvider?, data: D) {
        (player as IPlayerExtension).openMenu(original) {
            val codec = extendedCodecs[type] as StreamCodec<in RegistryFriendlyByteBuf, D>
            codec.encode(it, data)
        }
    }

}
