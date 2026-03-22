package net.jidb.to.base.fabric.client.platform

import net.jidb.to.base.client.platform.ScreenClientPlatformModule
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.MenuAccess
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

object ScreenFabricClientPlatformModule : ScreenClientPlatformModule() {

    override fun <M : AbstractContainerMenu, S> registerMenuScreen(modid: String, type: () -> MenuType<M>, factory: (M, Inventory, Component) -> S) where S : Screen, S : MenuAccess<M> = MenuScreens.register(type(), factory)

}
