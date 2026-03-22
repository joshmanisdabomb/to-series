package net.jidb.to.base.neoforge.platform

import net.jidb.to.base.platform.InventoryPlatformModule
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

object InventoryForgePlatformModule : InventoryPlatformModule() {

    override fun <T : AbstractContainerMenu> createMenuType(constructor: (id: Int, playerInventory: Inventory) -> T, features: FeatureFlagSet) = MenuType(constructor, features)

}
