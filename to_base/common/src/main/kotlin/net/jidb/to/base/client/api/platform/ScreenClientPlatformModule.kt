package net.jidb.to.base.client.api.platform

import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.MenuAccess
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

abstract class ScreenClientPlatformModule {

    abstract fun <M : AbstractContainerMenu, S> registerMenuScreen(modid: String, type: () -> MenuType<M>, factory: (menu: M, playerInventory: Inventory, title: Component) -> S) where S : Screen, S : MenuAccess<M>

}
