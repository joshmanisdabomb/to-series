package net.jidb.to.base.client.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.MenuAccess
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

/**
 * [SimpleLibrary] implementation that binds a [Screen] to the [MenuType] that opens it, and provides access to those bindings in one place.
 * The menu types themselves live in a common library, and are taken here as suppliers so that a screen can be declared without the type having been built yet.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.1.0
 */
open class ScreenLibrary(modid: String) : SimpleLibrary<ScreenLibrary.ScreenEntry<out AbstractContainerMenu, *>>(modid) {

    override fun afterBuild(entry: Library<ScreenEntry<out AbstractContainerMenu, *>, ScreenEntry<out AbstractContainerMenu, *>>.LibraryEntry<out ScreenEntry<out AbstractContainerMenu, *>, out ScreenEntry<out AbstractContainerMenu, *>>) = entry.value.register()

    /**
     * A screen declared in the outer [ScreenLibrary], pairing the type of menu it displays with the factory that builds it.
     * The pair is kept in an entry of its own rather than registered directly, so that the menu's type parameter survives the wildcard the library stores.
     *
     * @param M The type of the menu the screen displays.
     * @param S The type of the screen, which has to be both a [Screen] and a [MenuAccess] for the menu.
     * @param type A [MenuType] supplier that the screen is bound to.
     * @param factory A function that builds the screen, given the menu, the player's inventory and the title the menu was opened with.
     * @since 0.1.0
     */
    inner class ScreenEntry<M : AbstractContainerMenu, S>(private val type: () -> MenuType<M>, private val factory: (menu: M, playerInventory: Inventory, title: Component) -> S) where S : Screen, S : MenuAccess<M> {

        /**
         * Registers this screen with the client platform, which the outer library does once the entry has been built.
         *
         * @return [Unit]
         * @since 0.1.0
         */
        internal fun register() = ClientServices.platform.screens.registerMenuScreen(modid, type, factory)

    }

}
