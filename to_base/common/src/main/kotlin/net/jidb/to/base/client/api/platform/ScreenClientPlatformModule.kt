package net.jidb.to.base.client.api.platform

import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.MenuAccess
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

/**
 * A [ClientPlatform]-specific module that defines a cross-platform contract for handling screens.
 * This module has code for binding a [Screen] to the [MenuType] that opens it, which is what makes a menu opened on the server show a GUI on the client.
 *
 * @since 0.1.0
 */
abstract class ScreenClientPlatformModule {

    /**
     * Registers the [Screen] that the client opens when the server opens a menu of the given type.
     *
     * @param M The type of the menu the screen displays.
     * @param S The type of the screen, which has to be both a [Screen] and a [MenuAccess] for the menu.
     * @param modid The unique namespace of the mod registering the screen.
     * @param type A [MenuType] supplier that the screen is bound to.
     * @param factory A function that builds the screen, given the menu, the player's inventory and the title the menu was opened with.
     * @since 0.1.0
     */
    abstract fun <M : AbstractContainerMenu, S> registerMenuScreen(modid: String, type: () -> MenuType<M>, factory: (menu: M, playerInventory: Inventory, title: Component) -> S) where S : Screen, S : MenuAccess<M>

}
