package net.jidb.to.base.api.platform

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

abstract class InventoryPlatformModule {

    abstract fun <T : AbstractContainerMenu> createMenuType(constructor: (id: Int, playerInventory: Inventory) -> T, features: FeatureFlagSet): MenuType<T>

}
